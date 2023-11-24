package com.thomas.modules.chat.service;

import com.thomas.modules.chat.dto.MessageResponseDto;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
public interface MessageService {
    MessageEntity createMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto);
    MessageEntity updateMessage(MessageEntity message, String content, Optional<MessageEntity> reply, List<FileEntity> attachedFiles);

    Optional<MessageEntity> getMessageById(Long messageId);
    Optional<MessageEntity> getLastMessage(ChatEntity chat);


    Page<MessageEntity> getAllChatMessages(ChatEntity chat, Pageable pageable);
    long countChatMessages(ChatEntity chat);


    Page<MessageEntity> getAllChatMessagesBefore(ChatEntity chat, Timestamp origin, Pageable pageable);
    long countChatMessagesBefore(ChatEntity chat, Timestamp origin);


    Page<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable);
    Set<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end);
    long countChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end);


    Page<MessageEntity> getAllSenderChatMessages(ChatEntity chat, UserEntity sender, Pageable pageable);
    long countSenderChatMessages(ChatEntity chat, UserEntity sender);



    Page<MessageEntity> getAllSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable);
    long countSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end);


    Set<MessageEntity> getSenderUnreadMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end);
    long countSenderUnreadMessages(ChatEntity chat, UserEntity sender, Timestamp origin);


    void deleteMessage(MessageEntity message);
}
