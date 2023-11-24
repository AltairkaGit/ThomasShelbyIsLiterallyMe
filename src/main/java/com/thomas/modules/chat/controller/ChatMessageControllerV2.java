package com.thomas.modules.chat.controller;

import com.thomas.modules.chat.dto.*;
import com.thomas.modules.chat.dto.mapper.MessageMapper;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.service.ChatService;
import com.thomas.modules.chat.service.MessageSeenService;
import com.thomas.modules.chat.service.MessageService;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.security.websocket.ChatAuthorizationSubscription;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class ChatMessageControllerV2 {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private final FileService fileService;
    private final MessageService messageService;
    private final MessageSeenService messageSeenService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    public ChatMessageControllerV2(
            ChatService chatService,
            UserService userService,
            MessageMapper messageMapper,
            FileService fileService,
            MessageService messageService,
            MessageSeenService messageSeenService,
            SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.chatService = chatService;
        this.userService = userService;
        this.messageMapper = messageMapper;
        this.fileService = fileService;
        this.messageService = messageService;
        this.messageSeenService = messageSeenService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @ChatAuthorizationSubscription
    @SubscribeMapping("/queue/chat/{chatId}/messages")
    public void subscribeChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("chatId") String chatIdParam
    ) {
    }

    //for future optimizations
    @SubscribeMapping("/queue/chat/messages/new")
    public void subscribeNewMessages(
            SimpMessageHeaderAccessor accessor
    ) {
        //Long userId = (Long)accessor.getSessionAttributes().get("userId");

    }

    @ChatAuthorizationSubscription
    @MessageMapping("/chat/{chatId}")
    @Transactional
    public void sendMessage(
        SimpMessageHeaderAccessor accessor,
        @DestinationVariable("chatId") String chatIdParam,
        MessageWebsocketDto message
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        Long chatId = Long.valueOf(chatIdParam);
        ChatEntity chat = chatService.getById(chatId);
        UserEntity me = userService.getUserById(myId);

        MessageEntity messageFromDto = messageMapper.convert(message);
        MessageEntity messageEntity = messageService.createMessage(chat, me, messageFromDto);
        MessageResponseDto handledMessage = messageMapper.convert(messageEntity, me);
        simpMessagingTemplate.convertAndSend("/app/queue/chat/" + chatId + "/messages", handledMessage);
        notifyNewMessage(messageEntity, chat);
    }

    /**
     *  Format: chatId:messageId
     */
    @Transactional
    public void notifyNewMessage(MessageEntity message, ChatEntity chat) {
        //notify everyone except me
        String messageNotification = chat.getChatId() + ":" + message.getMessageId();
        String messageNewDest = "/app/queue/chat/messages/new";
        if (chatService.countChatUsers(chat) > 100) {
            Stream<Long> chatUserIds = chatService.streamAllChatUserIds(chat);
            chatUserIds
                    .filter(id -> !Objects.equals(id, message.getSender().getUserId()))
                    .map(String::valueOf)
                    .forEach(id -> simpMessagingTemplate.convertAndSendToUser(id, messageNewDest, messageNotification));
        } else {
            Set<Long> chatUserIds = chatService.getAllChatUserIds(chat);
            chatUserIds.remove(message.getSender().getUserId());
            chatUserIds.forEach(id -> simpMessagingTemplate.convertAndSendToUser(String.valueOf(id), messageNewDest, messageNotification));
        }
    }

    @PutMapping("/api/v2/message")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    @Operation(summary = "update message if you are sender, you should send complete dto(as message should be)")
    public ResponseEntity<MessageResponseDto> updateMessage(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody UpdateMessageDto dto
    ) {
        Optional<MessageEntity> messageOptional = messageService.getMessageById(dto.getMessageId());
        if (messageOptional.isEmpty())
            throw new IllegalArgumentException("no message with this id");
        MessageEntity message = messageOptional.get();
        if (!Objects.equals(myId, message.getSender().getUserId()))
            throw new IllegalArgumentException("you are not the sender of the message");
        MessageEntity updatedMessage = messageService.updateMessage(
                message,
                dto.getContent(),
                messageService.getMessageById(dto.getReplyId()),
                dto.getFileUrls().stream().map(fileService::getFile).collect(Collectors.toList())
        );
        UserEntity me = userService.getUserById(myId);
        MessageResponseDto updatedMessageDto = messageMapper.convert(updatedMessage, me);
        return ResponseEntity.ok(updatedMessageDto);
    }

    @DeleteMapping("/api/v2/message")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    @Operation(summary = "update message if you are sender")
    public ResponseEntity<Void> deleteMessage(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody DeleteMessageDto dto
    ) {
        Optional<MessageEntity> messageOptional = messageService.getMessageById(dto.getMessageId());
        if (messageOptional.isEmpty())
            throw new IllegalArgumentException("no message with this id");
        MessageEntity message = messageOptional.get();
        if (!Objects.equals(myId, message.getSender().getUserId()))
            throw new IllegalArgumentException("you are not the sender of the message");
        messageService.deleteMessage(message);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/v2/messages")
    @PreAuthorize("hasAuthority(T(com.thomas.roles.ChatRole).Participant.name())")
    @Operation(summary="read messages between start(like now) and end(like in the past)")
    public ResponseEntity<Void> readMessages(
            @RequestAttribute("reqUserId") Long myId,
            @RequestBody ReadChatMessagesDto dto
    ) {
        UserEntity me = userService.getUserById(myId);
        ChatEntity chat = chatService.getById(dto.getChatId());
        messageSeenService.readMessages(chat, me, dto.getStart(), dto.getEnd());
        return ResponseEntity.ok().build();
    }
}
