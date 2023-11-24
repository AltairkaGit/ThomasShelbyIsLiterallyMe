package com.thomas.modules.chat.dto.mapper;

import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import com.thomas.modules.chat.dto.ChatResponseDto;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.service.ChatService;
import com.thomas.modules.chat.service.MessageService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class ChatResponseMapper {
    @Autowired
    protected ChatService chatService;
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected MessageMapper messageMapper;
    @Autowired
    protected UserProfileResponseMapper userProfileResponseMapper;
    @Autowired
    protected FileResponseMapper fileResponseMapper;
    @Autowired
    protected FileService fileService;

    public ChatResponseDto convert(ChatEntity chat, UserEntity me) {
        ChatResponseDto chatResponseDto = new ChatResponseDto();

        chatResponseDto.setChatId(chat.getChatId());
        chatResponseDto.setChatType(chat.getChatType());
        chatResponseDto.setChatUsersCount(chatService.countChatUsers(chat));
        chatResponseDto.setUnreadMessages(chatService.countUnreadMessages(chat, me, Timestamp.from(Instant.now())));
        Optional<MessageEntity> lastMessage = messageService.getLastMessage(chat);
        lastMessage.ifPresent(message -> chatResponseDto.setLastMessage(messageMapper.convert(message, me)));

        // Set chatName and picture depends on type of the chat
        //direct chat
        if (chatResponseDto.getChatType() == ChatEntity.ChatType.direct) {
            Optional<UserEntity> userEntity = chatService.getChatUsers(chat)
                    .filter(user -> !user.equals(me))
                    .findFirst();
            if (userEntity.isPresent()) {
                UserEntity companion = userEntity.get();
                chatResponseDto.setChatName(companion.getUsername());
                Optional<FileEntity> picture = companion.getProfilePicture();
                chatResponseDto.setChatPictureUrl(fileResponseMapper.map(picture));
            }
        }
        //group chat
        else if (chatResponseDto.getChatType() == ChatEntity.ChatType.conversation) {
            chatResponseDto.setChatName(chat.getChatName());
            Optional<FileEntity> picture = chat.getChatPicture();
            chatResponseDto.setChatPictureUrl(fileResponseMapper.map(picture));
        }
        //still no picture
        if (chatResponseDto.getChatPictureUrl() == null) {
            chatResponseDto.setChatPictureUrl(fileService.composeUrl("defaultServer.png"));
        }
        //still no name
        if (chatResponseDto.getChatName() == null) {
            int count = 3;
            String chatName = chatService.getChatUsers(chat)
                            .map(UserEntity::getUsername)
                            .limit(count)
                            .collect(Collectors.joining(", "));
            if (chatService.getChatUsers(chat).limit(count + 1).count() > count)
                chatName += "...";
            chatResponseDto.setChatName(chatName);
        }

        return chatResponseDto;
    }

    public List<ChatResponseDto> convertList(List<ChatEntity> chats, UserEntity me) {
        return chats.stream()
                .map(chat -> convert(chat, me))
                .collect(Collectors.toList());
    }
    public Page<ChatResponseDto> convertPage(Page<ChatEntity> page, UserEntity me) {
        List<ChatResponseDto> list = convertList(page.getContent(), me);
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
