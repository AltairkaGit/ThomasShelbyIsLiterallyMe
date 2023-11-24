package com.thomas.modules.server.service;

import com.thomas.modules.server.entity.ServerChannelEntity;
import com.thomas.modules.server.entity.ServerEntity;

public interface ServerChannelService {
    ServerChannelEntity createChannel(ServerEntity server, String channelName, ServerChannelEntity.ChannelType channelType);
}
