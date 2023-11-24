package com.thomas.modules.file.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.thomas.modules.chat.entity.MessageEntity;
import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.chat.entity.ChatEntity;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "file")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    @Column(name = "filename", nullable = false)
    private String name;

    @Column(name = "file_size", nullable = false)
    private Integer size;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @OneToOne(mappedBy = "profilePicture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private UserEntity profilePictureOfUser;

    @OneToOne(mappedBy = "serverPicture", fetch = FetchType.LAZY)
    @JsonBackReference
    private ServerEntity serverPicture;

    @OneToOne(mappedBy = "chatPicture", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private ChatEntity chatPicture;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "attachedFiles", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MessageEntity> attachedMessages;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public ServerEntity getServerPicture() {
        return serverPicture;
    }

    public void setServerPicture(ServerEntity serverPicture) {
        this.serverPicture = serverPicture;
    }

    public ChatEntity getChatPicture() {
        return chatPicture;
    }

    public void setChatPicture(ChatEntity chatPicture) {
        this.chatPicture = chatPicture;
    }

    public UserEntity getProfilePictureOfUser() {
        return profilePictureOfUser;
    }

    public void setProfilePictureOfUser(UserEntity profilePictureOfUser) {
        this.profilePictureOfUser = profilePictureOfUser;
    }

    public List<MessageEntity> getAttachedMessages() {
        return attachedMessages;
    }

    public void setAttachedMessages(List<MessageEntity> attachedMessages) {
        this.attachedMessages = attachedMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        if (!Objects.equals(fileId, that.fileId)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(size, that.size)) return false;
        return Objects.equals(mimeType, that.mimeType);
    }

    @Override
    public int hashCode() {
        int result = fileId != null ? fileId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (mimeType != null ? mimeType.hashCode() : 0);
        return result;
    }
}
