package com.thomas.modules.chat.entity;

import com.thomas.modules.chat.entity.key.MessageSeenKey;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "message_seen")
public class MessageSeenEntity {
    @EmbeddedId
    private MessageSeenKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("messageId")
    @JoinColumn(name = "message_id")
    private MessageEntity message;

    @Column(name="seen_timestamp", nullable = false)
    private Timestamp seenTimestamp;

    public MessageSeenEntity() {
    }

    public MessageSeenEntity(MessageEntity message, UserEntity user) {
        id = MessageSeenKey.valueOf(user.getUserId(), message.getMessageId());
        this.user = user;
        this.message = message;
    }

    public MessageSeenKey getId() {
        return id;
    }

    public void setId(MessageSeenKey id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }

    public Timestamp getSeenTimestamp() {
        return seenTimestamp;
    }

    public void setSeenTimestamp(Timestamp seenTimestamp) {
        this.seenTimestamp = seenTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageSeenEntity that = (MessageSeenEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(user, that.user) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, message);
    }
}
