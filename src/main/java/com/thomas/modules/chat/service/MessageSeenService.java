package com.thomas.modules.chat.service;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Transactional
public interface MessageSeenService {
    void readMessages(ChatEntity chat, UserEntity user, Timestamp start, Timestamp end);
    long getMessageViews(MessageEntity message);
    Page<UserEntity> getMessageViewers(MessageEntity message, Pageable pageable);
}
