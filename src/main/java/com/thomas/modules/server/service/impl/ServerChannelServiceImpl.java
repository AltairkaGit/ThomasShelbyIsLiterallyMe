package com.thomas.modules.server.service.impl;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.repos.ChatRepository;
import com.thomas.modules.server.entity.ServerChannelEntity;
import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.server.repos.ServerChannelRepository;
import com.thomas.modules.server.service.ServerChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServerChannelServiceImpl implements ServerChannelService {
    private final ServerChannelRepository serverChannelRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public ServerChannelServiceImpl(ServerChannelRepository serverChannelRepository, ChatRepository chatRepository) {
        this.serverChannelRepository = serverChannelRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public ServerChannelEntity createChannel(ServerEntity server, String channelName, ServerChannelEntity.ChannelType channelType) {
        ChatEntity channelChat = new ChatEntity();
        channelChat.setChatType(ChatEntity.ChatType.conversation);
        chatRepository.saveAndFlush(channelChat);

        ServerChannelEntity channel = new ServerChannelEntity();
        channel.setChat(channelChat);
        channel.setServer(server);
        channel.setChannelName(channelName);
        channel.setChannelType(channelType);
        serverChannelRepository.saveAndFlush(channel);

        return channel;
    }
}
