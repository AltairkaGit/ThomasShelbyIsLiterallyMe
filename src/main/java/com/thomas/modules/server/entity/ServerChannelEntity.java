package com.thomas.modules.server.entity;

import com.thomas.modules.chat.entity.ChatEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_channel")
public class ServerChannelEntity {
    @Schema
    public enum ChannelType {text, voice}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long channelId;
    @Column(name = "channel_name")
    private String channelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;

    @Column(name = "channel_type")
    private String channelType;

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public ChatEntity getChat() {
        return chat;
    }

    public void setChat(ChatEntity chat) {
        this.chat = chat;
    }

    public ChannelType getChannelType() {
        return ChannelType.valueOf(channelType);
    }

    public void setChannelType(ChannelType channelType) {
        this.channelType = channelType.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerChannelEntity that = (ServerChannelEntity) o;
        return Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channelId);
    }
}
