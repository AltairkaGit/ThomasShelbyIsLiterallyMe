package com.thomas.modules.chat.dto.mapper;

import com.thomas.modules.chat.dto.MessageResponseDto;
import com.thomas.modules.chat.dto.MessageWebsocketDto;
import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.service.MessageService;
import com.thomas.modules.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", uses = {UserProfileResponseMapper.class, FileResponseMapper.class})
public abstract class MessageMapper {
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected FileService fileService;
    @Autowired
    protected FileResponseMapper fileResponseMapper;

    public MessageResponseDto convert(MessageEntity message, UserEntity receiver) {
        MessageResponseDto dto = new MessageResponseDto();

        dto.setChatId(message.getChat().getChatId());
        dto.setMessageId(message.getMessageId());
        dto.setContent(message.getContent());
        dto.setSendTimestamp(message.getSendTimestamp());
        dto.setSenderId(message.getSender().getUserId());
        dto.setFileUrls(fileResponseMapper.map(message.getAttachedFiles()));

        MessageEntity reply = message.getReply();
        if (reply != null) {
            dto.setReplyId(reply.getMessageId());
            String replyContent = reply.getContent();
            int replyContentLen = 35;
            dto.setReplyContent(replyContent.length() > replyContentLen ? replyContent.substring(0, replyContentLen) : replyContent);
        }

        return  dto;
    }

    public MessageEntity convert(MessageWebsocketDto dto) {
        MessageEntity message = extractBaseMessageEntity(dto.getContent(), dto.getSendTimestamp(), dto.getReplyId());

        if (dto.getFileIds() != null)
            message.setAttachedFiles(dto.getFileIds().stream().map(fileId -> fileService.getFile(fileId)).collect(Collectors.toList()));
        return message;
    }

    public MessageEntity convert(MessageResponseDto dto) {
        MessageEntity message = extractBaseMessageEntity(dto.getContent(), dto.getSendTimestamp(), dto.getReplyId());

        if (dto.getFileUrls() != null)
            message.setAttachedFiles(dto.getFileUrls().stream().map(fileUrl -> fileService.getFile(fileUrl)).collect(Collectors.toList()));
        return message;
    }

    private MessageEntity extractBaseMessageEntity(String content, Timestamp sendTimestamp, Long replyId) {
        MessageEntity message = new MessageEntity();

        message.setContent(content);
        message.setSendTimestamp(sendTimestamp);
        if (replyId != null) {
            Optional<MessageEntity> reply = messageService.getMessageById(replyId);
            reply.ifPresent(message::setReply);
         }
        return message;
    }

    @Named("isMineMessage")
    public boolean setIsMine(MessageEntity message, UserEntity receiver) {
        return message.getSender().equals(receiver);
    }

    public List<MessageResponseDto> convertList(List<MessageEntity> messageEntities, UserEntity receiver) {
        return messageEntities.stream().map(message -> convert(message, receiver)).collect(Collectors.toList());
    }
    public Page<MessageResponseDto> convertPage(Page<MessageEntity> page, UserEntity receiver) {
        List<MessageResponseDto> list = convertList(page.getContent(), receiver);
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
