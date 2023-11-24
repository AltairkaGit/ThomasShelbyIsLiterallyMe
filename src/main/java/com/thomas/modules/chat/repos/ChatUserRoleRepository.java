package com.thomas.modules.chat.repos;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.entity.ChatUserRoleEntity;
import com.thomas.modules.chat.entity.key.ChatUserRoleKey;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.roles.ChatRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatUserRoleRepository extends JpaRepository<ChatUserRoleEntity, ChatUserRoleKey> {
    Optional<ChatUserRoleEntity> findByChatAndUserAndIdRole(ChatEntity chat, UserEntity user, ChatRole role);
    Optional<ChatUserRoleEntity> findTopByChatAndIdRole(ChatEntity chat, ChatRole role);
    Set<ChatUserRoleEntity> findAllByChatAndIdRole(ChatEntity chat, ChatRole role);

    @Query("SELECT cur.id.role FROM ChatUserRoleEntity cur WHERE cur.chat.chatId = :chatId AND cur.user.userId = :userId")
    List<ChatRole> findChatUserRoles(
            @Param("chatId") Long chatId,
            @Param("userId") Long userId
    );

    void deleteAllByUserAndChat(UserEntity user, ChatEntity chat);
    void deleteAllByUserInAndChat(Collection<UserEntity> users, ChatEntity chat);
}
