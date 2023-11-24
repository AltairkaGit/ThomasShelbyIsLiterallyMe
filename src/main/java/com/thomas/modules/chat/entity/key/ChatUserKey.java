package com.thomas.modules.chat.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChatUserKey implements Serializable {
    public static ChatUserKey valueOf(Long userId, Long chatId) {
        ChatUserKey res = new ChatUserKey();
        res.setUserId(userId);
        res.setChatId(chatId);
        return res;
    }
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "chat_id")
    private Long chatId;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getChatId() {
        return chatId;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUserKey that = (ChatUserKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(chatId, that.chatId);
    }
    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId);
    }
}
