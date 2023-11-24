package com.thomas.modules.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thomas.modules.file.entity.FileEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "chat")
public class ChatEntity {
    @Schema
    public enum ChatType {direct, conversation}
    @Converter(autoApply = true)
    public static class ChatTypeConverter implements AttributeConverter<ChatType, String> {

        @Override
        public String convertToDatabaseColumn(ChatType attribute) {
            return attribute != null ? attribute.name() : null;
        }

        @Override
        public ChatType convertToEntityAttribute(String dbData) {
            return dbData != null ? ChatType.valueOf(dbData) : null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id", nullable = false)
    private Long chatId;
    @Column(name = "chat_name", nullable = false)
    private String chatName;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    @JsonManagedReference
    private FileEntity chatPicture;

    @Column(name = "chat_type", nullable = false)
    @Convert(converter = ChatTypeConverter.class)
    private ChatType chatType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "chat", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<ChatUserEntity> chatUsers;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id")
    @JsonBackReference
    private Set<MessageEntity> messages;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long serverId) {
        this.chatId = serverId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public Optional<FileEntity> getChatPicture() {
        if (chatPicture == null) return Optional.empty();
        return Optional.of(chatPicture);
    }

    public void setChatPicture(FileEntity chatPicture) {
        this.chatPicture = chatPicture;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public void setChatUsers(Set<ChatUserEntity> chatUsers) {
        this.chatUsers = chatUsers;
    }

    public void setMessages(Set<MessageEntity> messages) {
        this.messages = messages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatEntity that = (ChatEntity) o;
        return Objects.equals(chatId, that.chatId) && Objects.equals(chatName, that.chatName) && Objects.equals(chatPicture, that.chatPicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, chatName, chatPicture);
    }
}
