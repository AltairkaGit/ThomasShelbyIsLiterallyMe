package com.thomas.modules.chat.repos;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Page<MessageEntity> findAllByChat(ChatEntity chat, Pageable pageable);
    long countAllByChat(ChatEntity chat);
    Page<MessageEntity> findAllByChatAndSenderOrderBySendTimestampDesc(ChatEntity chat, UserEntity sender, Pageable pageable);
    long countAllByChatAndSender(ChatEntity chat, UserEntity sender);
    Page<MessageEntity> findAllByChatAndSendTimestampBetweenOrderBySendTimestampDesc(ChatEntity chat, Timestamp start, Timestamp end, Pageable pageable);
    long countAllByChatAndSendTimestampBetween(ChatEntity chat, Timestamp start, Timestamp end);
    Page<MessageEntity> findAllByChatAndSenderAndSendTimestampBetweenOrderBySendTimestampDesc(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end, Pageable pageable);
    long countAllByChatAndSenderAndSendTimestampBetween(ChatEntity chat, UserEntity sender, Timestamp start, Timestamp end);
    Page<MessageEntity> findAllByChatAndSendTimestampBeforeOrderBySendTimestampDesc(ChatEntity chat, Timestamp start, Pageable pageable);
    long countAllByChatAndSendTimestampBefore(ChatEntity chat, Timestamp start);
    @Query( "SELECT m FROM MessageEntity m " +
            "WHERE m.chat.chatId = :chatId " +
            "  AND m.sendTimestamp = (SELECT MAX(mes.sendTimestamp) FROM MessageEntity mes WHERE mes.chat.chatId = :chatId) " +
            " "
    )
    List<MessageEntity> getLastMessages(@Param("chatId") long chatId);
    Set<MessageEntity> findAllByChatAndSendTimestampBetweenOrderBySendTimestampDesc(ChatEntity chat, Timestamp start, Timestamp end);
    @Query("SELECT DISTINCT COUNT(mes.messageId) " +
            "FROM MessageEntity mes " +
            "LEFT JOIN MessageSeenEntity ms ON mes.messageId = ms.message.messageId " +
            "WHERE mes.chat.chatId = :chatId " +
            "AND mes.sendTimestamp <= :paginationOriginTimestamp " +
            "AND mes.sender.userId != :userId " +
            "AND (ms.user.userId is null OR ms.user.userId != :userId)")
    long countUnreadMessages(
            @Param("chatId") Long chatId,
            @Param("userId") Long userId,
            @Param("paginationOriginTimestamp") Timestamp paginationOriginTimestamp
    );

    @Query("SELECT DISTINCT mes " +
            "FROM MessageEntity mes " +
            "LEFT JOIN MessageSeenEntity ms ON mes.messageId = ms.message.messageId " +
            "WHERE mes.chat.chatId = :chatId " +
            "AND mes.sendTimestamp <= :startIntervalTimestamp " +
            "AND mes.sendTimestamp >= :endIntervalTimestamp " +
            "AND mes.sender.userId != :userId " +
            "AND (ms.user.userId is null OR ms.user.userId != :userId)")
    Set<MessageEntity> findUnreadMessagesBetween(
            @Param("chatId") Long chatId,
            @Param("userId") Long userId,
            @Param("startIntervalTimestamp") Timestamp startIntervalTimestamp,
            @Param("endIntervalTimestamp") Timestamp endIntervalTimestamp
    );
}
