package com.thomas.modules.chat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="message")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id", nullable = false)
    private Long messageId;
    @Column(name="send_timestamp", nullable = false)
    private Timestamp sendTimestamp;
    @Column(name="message_content", nullable = false)
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    @JsonManagedReference
    private ChatEntity chat;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    @JsonManagedReference
    private UserEntity sender;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "replying_message_id")
    @JsonManagedReference
    private MessageEntity reply;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "message_file",
            joinColumns = {@JoinColumn(name = "message_id", referencedColumnName = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "file_id", referencedColumnName = "file_id")})
    @JsonManagedReference
    private List<FileEntity> attachedFiles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message")
    @JsonBackReference
    private Set<MessageSeenEntity> lookedUsers;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Timestamp getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Timestamp sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public MessageEntity getReply() {
        return reply;
    }

    public void setReply(MessageEntity reply) {
        this.reply = reply;
    }

    public List<FileEntity> getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(List<FileEntity> attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public Set<MessageSeenEntity> getLookedUsers() {
        return lookedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return Objects.equals(messageId, that.messageId) && Objects.equals(content, that.content) && Objects.equals(chat, that.chat) && Objects.equals(sender, that.sender) && Objects.equals(reply, that.reply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId, sendTimestamp, content, chat, sender, reply);
    }

    @Override
    public String toString() {
        return "MessageEntity{" +
                "messageId=" + messageId +
                ", sendTimestamp=" + sendTimestamp +
                ", content='" + content + '\'' +
                ", chat=" + chat +
                ", sender=" + sender +
                ", reply=" + reply +
                '}';
    }
}
