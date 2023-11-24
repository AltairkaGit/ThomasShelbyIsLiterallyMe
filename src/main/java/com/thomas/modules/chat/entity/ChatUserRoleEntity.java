package com.thomas.modules.chat.entity;

import com.thomas.modules.chat.entity.key.ChatUserRoleKey;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.roles.ChatRole;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="chat_user_role")
public class ChatUserRoleEntity {
    @EmbeddedId
    private ChatUserRoleKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    public ChatUserRoleEntity() {
    }

    public ChatUserRoleEntity(UserEntity user, ChatEntity chat, ChatRole role) {
        this.id = ChatUserRoleKey.ValueOf(chat.getChatId(), user.getUserId(), role);
        this.user = user;
        this.chat = chat;
    }

    public ChatUserRoleKey getId() {
        return id;
    }

    public void setId(ChatUserRoleKey id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    public ChatRole getRole() {
        return id.getRole();
    }

    public void setRole(ChatRole role) {
        this.id.setRole(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUserRoleEntity that = (ChatUserRoleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
