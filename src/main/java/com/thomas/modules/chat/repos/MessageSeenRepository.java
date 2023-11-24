package com.thomas.modules.chat.repos;

import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.chat.entity.MessageSeenEntity;
import com.thomas.modules.chat.entity.key.MessageSeenKey;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface MessageSeenRepository extends JpaRepository<MessageSeenEntity, MessageSeenKey> {
    Stream<MessageSeenEntity> findAllByMessage(MessageEntity message);
    @Query("SELECT ms.user FROM MessageSeenEntity ms WHERE ms.message = :message")
    Page<UserEntity> findAllByMessage(MessageEntity message, Pageable pageable);
    long countByMessage(MessageEntity message);
}
