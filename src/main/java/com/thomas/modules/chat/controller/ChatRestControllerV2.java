package com.thomas.modules.chat.controller;

import com.thomas.lib.page.PageDto;
import com.thomas.modules.chat.dto.*;
import com.thomas.modules.chat.dto.mapper.ChatResponseMapper;
import com.thomas.modules.chat.dto.mapper.ChatUserResponseMapper;
import com.thomas.modules.chat.dto.mapper.MessageMapper;
import com.thomas.modules.chat.dto.mapper.RemoveUsersFromChatMapper;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.exception.UserAlreadyInTheChatException;
import com.thomas.modules.chat.exception.UserNotInTheChatException;
import com.thomas.modules.chat.service.ChatService;
import com.thomas.modules.chat.service.MessageService;
import com.thomas.modules.security.chatAuthorization.ChatAuthorizationService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v2/chat")
public class ChatRestControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageService messageService;
    private final ChatResponseMapper chatResponseMapper;
    private final MessageMapper messageMapper;
    private final ChatUserResponseMapper chatUserResponseMapper;
    private final RemoveUsersFromChatMapper removeUsersFromChatMapper;
    private final ChatAuthorizationService chatAuthorizationService;

    @Autowired
    public ChatRestControllerV2(
            ChatService chatService,
            UserService userService,
            MessageService messageService,
            ChatResponseMapper chatResponseMapper,
            MessageMapper messageMapper,
            ChatUserResponseMapper chatUserResponseMapper,
            RemoveUsersFromChatMapper removeUsersFromChatMapper,
            ChatAuthorizationService chatAuthorizationService) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageService = messageService;
        this.chatResponseMapper = chatResponseMapper;
        this.messageMapper = messageMapper;
        this.chatUserResponseMapper = chatUserResponseMapper;
        this.removeUsersFromChatMapper = removeUsersFromChatMapper;
        this.chatAuthorizationService = chatAuthorizationService;
    }

    @Operation(summary = "Get a page of your chats")
    @GetMapping(value="")
    public ResponseEntity<PageDto<ChatResponseDto>> getChats(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ChatEntity> chats = chatService.getUserChats(me, pageable);
        PageDto<ChatResponseDto> res = PageDto.of(chatResponseMapper.convertPage(chats, me));
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "Get a chat by id, you should be a participant")
    @GetMapping(value="/{chatId}")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    public ResponseEntity<ChatResponseDto> getChat(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId
    ) {
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(userId);
        ChatResponseDto res = chatResponseMapper.convert(chat, me);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create a group chat")
    @PostMapping("/group")
    public ResponseEntity<ChatResponseDto> createGroupChat(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody CreateGroupChatRequestDto dto
    ) {
        Set<UserEntity> users = userService.getUsersByUsername(dto.getUsernames());
        UserEntity me = userService.getUserById(userId);
        ChatEntity chat = chatService.createGroupChat(dto.getName(), me, users);
        ChatResponseDto res = chatResponseMapper.convert(chat, me);
        return ResponseEntity.ok(res);
    }

    @Operation(summary = "create a direct chat, 201 if created, 200 if the chat exists, 400 if there is no partner with such username")
    @PostMapping("/direct")
    public ResponseEntity<ChatResponseDto> createDirectChat(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody CreateDirectChatRequserDto dto
    ) throws UserAlreadyInTheChatException {
        UserEntity partner;
        try {
            partner = userService.getUserByUsername(dto.getUsername());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        UserEntity me = userService.getUserById(userId);
        if (me.equals(partner)) {
            throw new IllegalArgumentException("you cannot create direct with yourself");
        }
        Optional<ChatEntity> chatEntityOptional = chatService.getDirectByUsers(me, partner);
        ChatResponseDto res;
        if (chatEntityOptional.isPresent()) {
            res = chatResponseMapper.convert(chatEntityOptional.get(), me);
            return ResponseEntity.ok(res);
        }
        ChatEntity chat = chatService.craeteDirectChat(me,partner);
        res = chatResponseMapper.convert(chat, me);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/{chatId}/leave")
    @Operation(summary = "leave a chat if you are a participant")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    public ResponseEntity<Void> leaveChat(
            @RequestAttribute("reqUserId") Long myId,
            @PathVariable Long chatId
    ) throws UserNotInTheChatException {
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(myId);
        chatService.removeUser(chat, me);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}")
    @Operation(summary = "delete a chat if you are an admin")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Admin.name())")
    public ResponseEntity<Void> deleteChat(
            @RequestAttribute("reqUserId") Long myId,
            @PathVariable Long chatId
    ) throws UserNotInTheChatException {
        ChatEntity chat = chatService.getById(chatId);
        chatService.delete(chat);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{chatId}/users")
    @Operation(summary = "kick users from a chat, you should be admin or moderator")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Admin.name()) || hasAuthority(T(com.thomas.roles.ChatRole).Moderator.name())")
    public ResponseEntity<Void> removeFromChat(
            @RequestAttribute("reqUserId") Long myId,
            @PathVariable Long chatId,
            @RequestBody RemoveUsersFromChatRequestDto ids
            ) throws UserNotInTheChatException {
        ChatEntity chat = chatService.getById(chatId);
        Set<UserEntity> users = removeUsersFromChatMapper.convert(ids);
        chatService.removeUsers(chat, users);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/{chatId}/messages")
    @Operation(summary = "get messages from the chat, you should be a participant")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    public ResponseEntity<PageDto<MessageResponseDto>> getChatMessages(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        ChatEntity chat = chatService.getById(chatId);
        Page<MessageEntity> messages = messageService.getAllChatMessages(chat, pageable);
        PageDto<MessageResponseDto> res = PageDto.of(messageMapper.convertPage(messages, me));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping(value="/{chatId}/users")
    @Operation(summary = "get users of the chat, you should be a participant")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    public ResponseEntity<PageDto<ChatUserProfileResponseDto>> getChatUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId,
            Pageable pageable
    ) {
        ChatEntity chat = chatService.getById(chatId);
        Page<UserEntity> users = chatService.getChatUsers(chat, pageable);
        PageDto<ChatUserProfileResponseDto> res = PageDto.of(chatUserResponseMapper.convertPage(chat, users));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping(value="/{chatId}/moderators")
    @Operation(summary = "add chat moderator, you should be the admin")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Admin.name())")
    public ResponseEntity<Void> addChatModerator(
            @RequestBody AddChatModeratorRequestDto dto,
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long chatId
    ) {
        ChatEntity chat = chatService.getById(chatId);
        if (!chatAuthorizationService.checkUserInChat(chat.getChatId(), dto.getUserId()))
            throw new IllegalArgumentException("user you want be a moderator is not in the chat");

        UserEntity user = userService.getUserById(dto.getUserId());
        chatService.addChatModerator(chat, user);

        return ResponseEntity.ok().build();
    }
}
