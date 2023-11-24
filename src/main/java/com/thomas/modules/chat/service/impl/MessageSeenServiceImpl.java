package com.thomas.modules.chat.service.impl;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.entity.MessageSeenEntity;
import com.thomas.modules.chat.repos.MessageRepository;
import com.thomas.modules.chat.repos.MessageSeenRepository;
import com.thomas.modules.chat.service.MessageSeenService;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageSeenServiceImpl implements MessageSeenService {
    private final MessageRepository messageRepository;
    private final MessageSeenRepository messageSeenRepository;

    @Autowired
    public MessageSeenServiceImpl(MessageRepository messageRepository, MessageSeenRepository messageSeenRepository) {
        this.messageRepository = messageRepository;
        this.messageSeenRepository = messageSeenRepository;
    }

    /**
     *
     * @param chat
     * @param user
     * @param start timestamp closer to now
     * @param end timestamp farther to now
     */
    @Override
    public void readMessages(ChatEntity chat, UserEntity user, Timestamp start, Timestamp end) {
        Set<MessageEntity> unreadMessageIds = messageRepository.findUnreadMessagesBetween(chat.getChatId(), user.getUserId(), start, end);
        List<MessageSeenEntity> views = unreadMessageIds.stream().map(message -> new MessageSeenEntity(message, user)).collect(Collectors.toList());
        messageSeenRepository.saveAllAndFlush(views);
    }

    @Override
    public long getMessageViews(MessageEntity message) {
        return messageSeenRepository.countByMessage(message);
    }

    @Override
    public Page<UserEntity> getMessageViewers(MessageEntity message, Pageable pageable) {
        return messageSeenRepository.findAllByMessage(message, pageable);
    }
}
