package com.thomas.modules.server.dto;

import com.thomas.modules.server.entity.ServerChannelEntity;

public class CreateChannelDto {
    String channelName;
    ServerChannelEntity.ChannelType channelType;

    public String getChannelName() {
        return channelName;
    }

    public ServerChannelEntity.ChannelType getChannelType() {
        return channelType;
    }
}
