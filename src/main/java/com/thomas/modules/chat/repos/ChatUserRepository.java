package com.thomas.modules.chat.repos;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.ChatUserEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.entity.key.ChatUserKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, ChatUserKey> {
    @Transactional(readOnly = true)
    Stream<ChatUserEntity> findAllByChat(ChatEntity chat);
    @Query( "SELECT chu.user.userId FROM ChatUserEntity chu " +
            "WHERE chu.chat = :chat "
    )
    Set<Long> findAllUserIdsByChat(@Param("chat") ChatEntity chat);
    @Query( "SELECT chu.user.userId FROM ChatUserEntity chu " +
            "WHERE chu.chat = :chat "
    )
    @Transactional(readOnly = true)
    Stream<Long> streamAllUserIdsByChat(@Param("chat") ChatEntity chat);
    @Query( "SELECT cu.user FROM ChatUserEntity cu ")
    Page<UserEntity> findChatUsers(ChatEntity chat, Pageable pageable);

    @Query( "SELECT ch FROM ChatEntity ch " +
            "INNER JOIN ChatUserEntity chu " +
            "ON ch.chatId = chu.id.chatId " +
            "WHERE chu.user = :user " +
            "ORDER BY (SELECT MAX(mes.sendTimestamp) " +
            "          FROM MessageEntity mes " +
            "          WHERE mes.chat = ch ) " +
            "DESC "
    )
    Page<ChatEntity> findUserChats(@Param("user") UserEntity user, Pageable pageable);
    Optional<ChatUserEntity> findByChatAndUser(ChatEntity chat, UserEntity user);
    Optional<ChatUserEntity> findTopByChat(ChatEntity chat);
    @Query( "SELECT ch FROM ChatEntity ch " +
            "INNER JOIN ChatUserEntity chu " +
            "ON ch.chatId = chu.id.chatId " +
            "WHERE ch.chatType = 'direct' AND chu.user = :u1 " +
            "AND chu.chat.chatId IN (SELECT chat.chatId FROM ChatEntity chat " +
            "                 INNER JOIN ChatUserEntity chatu ON chat.chatId = chatu.id.chatId " +
            "                 WHERE chat.chatType = 'direct' AND chatu.user = :u2 ) "
    )
    Optional<ChatEntity> findDirectByUsers(
            @Param("u1") UserEntity u1,
            @Param("u2") UserEntity u2
    );
    long countByChat(ChatEntity chat);
    default Stream<UserEntity> findChatUsers(ChatEntity chat) {
        return  findAllByChat(chat).map(ChatUserEntity::getUser);
    }
    void deleteAllByUserInAndChat(Collection<UserEntity> users, ChatEntity chat);
}