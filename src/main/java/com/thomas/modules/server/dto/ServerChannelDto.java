package com.thomas.modules.server.dto;

import com.thomas.modules.server.entity.ServerChannelEntity;

public class ServerChannelDto {
    long channelId;
    String channelName;
    long chatId;
    ServerChannelEntity.ChannelType channelType;

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public ServerChannelEntity.ChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(ServerChannelEntity.ChannelType channelType) {
        this.channelType = channelType;
    }
}
