package com.thomas.modules.chat.entity;

import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.entity.key.ChatUserKey;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "chat_user")
public class ChatUserEntity {
    @EmbeddedId
    private ChatUserKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("chatId")
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    public ChatUserEntity() {
    }

    public ChatUserEntity(ChatEntity chat, UserEntity user) {
        id = ChatUserKey.valueOf(user.getUserId(), chat.getChatId());
        setUser(user);
        setChat(chat);
    }

    public ChatUserKey getId() {
        return id;
    }

    public void setId(ChatUserKey id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUserEntity that = (ChatUserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(chat, that.chat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, chat);
    }
}
