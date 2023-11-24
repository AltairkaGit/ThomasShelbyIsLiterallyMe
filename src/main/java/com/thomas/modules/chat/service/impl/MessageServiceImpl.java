package com.thomas.modules.chat.service.impl;

import com.thomas.modules.chat.dto.MessageResponseDto;
import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.repos.MessageRepository;
import com.thomas.modules.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageEntity createMessage(ChatEntity chat, UserEntity sender, MessageEntity messageFromDto) {
        messageFromDto.setSender(sender);
        messageFromDto.setChat(chat);
        return messageRepository.save(messageFromDto);
    }

    @Override
    public MessageEntity updateMessage(MessageEntity message, String content, Optional<MessageEntity> reply, List<FileEntity> attachedFiles) {
        if (content == null || content.isEmpty())
            throw new IllegalArgumentException("no content of the message");

        message.setContent(content);
        reply.ifPresent(message::setReply);
        message.setAttachedFiles(attachedFiles);

        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(MessageEntity message) {
        messageRepository.delete(message);
    }

    @Override
    public Optional<MessageEntity> getMessageById(Long messageId) {
        return messageRepository.findById(messageId);
    }

    @Override
    public Optional<MessageEntity> getLastMessage(ChatEntity chat) {
        List<MessageEntity> lastMessages = messageRepository.getLastMessages(chat.getChatId());
        if (lastMessages == null || lastMessages.isEmpty()) return Optional.empty();
        return Optional.of(lastMessages.get(lastMessages.size() - 1));
    }

    @Override
    public Page<MessageEntity> getAllChatMessages(ChatEntity chat, Pageable pageable) {
        return messageRepository.findAllByChat(chat, pageable);
    }
    @Override
    public long countChatMessages(ChatEntity chat) {
        return messageRepository.countAllByChat(chat);
    }

    @Override
    public Page<MessageEntity> getAllChatMessagesBefore(ChatEntity chat, Timestamp origin, Pageable pageable) {
        return messageRepository.findAllByChatAndSendTimestampBeforeOrderBySendTimestampDesc(chat, origin, pageable);
    }
    @Override
    public long countChatMessagesBefore(ChatEntity chat, Timestamp origin) {
        return messageRepository.countAllByChatAndSendTimestampBefore(chat, origin);
    }

    @Override
    public Page<MessageEntity> getAllSenderChatMessages(ChatEntity chat, UserEntity sender, Pageable pageable) {
        return messageRepository.findAllByChatAndSenderOrderBySendTimestampDesc(chat, sender, pageable);
    }
    @Override
    public long countSenderChatMessages(ChatEntity chat, UserEntity sender) {
        return messageRepository.countAllByChatAndSender(chat, sender);
    }

    @Override
    public Set<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end) {
        return messageRepository.findAllByChatAndSendTimestampBetweenOrderBySendTimestampDesc(chat, start, end);
    }
    @Override
    public Page<MessageEntity> getAllChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable) {
        return messageRepository.findAllByChatAndSendTimestampBetweenOrderBySendTimestampDesc(chat, start, end, pageable);
    }
    @Override
    public long countChatMessagesWithTimeInterval(ChatEntity chat, Timestamp start, Timestamp end) {
        return messageRepository.countAllByChatAndSendTimestampBetween(chat, start, end);
    }

    @Override
    public Page<MessageEntity> getAllSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable) {
        return messageRepository.findAllByChatAndSenderAndSendTimestampBetweenOrderBySendTimestampDesc(chat, sender, start, end, pageable);
    }
    @Override
    public long countSenderChatMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end) {
        return messageRepository.countAllByChatAndSenderAndSendTimestampBetween(chat, sender, start, end);
    }

    @Override
    public Set<MessageEntity> getSenderUnreadMessagesWithTimeInterval(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end) {
        return messageRepository.findUnreadMessagesBetween(chat.getChatId(), sender.getUserId(), start, end);
    }
    @Override
    public long countSenderUnreadMessages(ChatEntity chat, UserEntity sender, Timestamp origin) {
        return messageRepository.countUnreadMessages(chat.getChatId(), sender.getUserId(), origin);
    }
}
