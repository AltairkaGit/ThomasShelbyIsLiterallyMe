package com.thomas.modules.chat.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MessageSeenKey implements Serializable {
    public static MessageSeenKey valueOf(Long userId, Long messageId) {
        MessageSeenKey key = new MessageSeenKey();
        key.setUserId(userId);
        key.setMessageId(messageId);
        return key;
    }
    @Column(name = "message_id")
    private Long messageId;
    @Column(name = "user_id")
    private Long userId;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageSeenKey that = (MessageSeenKey) o;
        return Objects.equals(messageId, that.messageId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, userId);
    }
}
