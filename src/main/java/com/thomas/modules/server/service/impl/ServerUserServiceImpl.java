package com.thomas.modules.server.service.impl;

import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.server.entity.ServerUserEntity;
import com.thomas.modules.server.exception.UserAlreadyOnServerException;
import com.thomas.modules.server.exception.UserNotOnServerException;
import com.thomas.modules.server.repos.ServerUserRepository;
import com.thomas.modules.server.service.ServerUserService;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ServerUserServiceImpl implements ServerUserService {
    private final ServerUserRepository serverUserRepository;

    @Autowired
    public ServerUserServiceImpl(ServerUserRepository serverUserRepository) {
        this.serverUserRepository = serverUserRepository;
    }

    @Override
    public Optional<ServerUserEntity> getServerUser(long serverId, long userId) {
        return serverUserRepository.findByServerServerIdAndUserUserId(serverId, userId);
    }

    @Override
    public void addUser(long serverId, long userId) throws UserAlreadyOnServerException {
        Optional<ServerUserEntity> serverUser = serverUserRepository.findByServerServerIdAndUserUserId(serverId, userId);
        if (serverUser.isPresent()) throw new UserAlreadyOnServerException(serverId, userId);
        serverUserRepository.createServerUser(serverId, userId);
    }

    @Override
    public void kickUser(long serverId, long userId) throws UserNotOnServerException {
        Optional<ServerUserEntity> serverUser = serverUserRepository.findByServerServerIdAndUserUserId(serverId, userId);
        if (serverUser.isEmpty()) throw new UserNotOnServerException(serverId, userId);
        serverUserRepository.delete(serverUser.get());
    }

    @Override
    public void kickUsers(long serverId, Collection<Long> userIds) {
        serverUserRepository.deleteAllByUserUserIdInAndServerServerId(userIds, serverId);
    }

    @Override
    public Page<ServerUserEntity> getServerUsers(ServerEntity server, Pageable pageable) {
        return serverUserRepository.findServerUsers(server, pageable);
    }

    @Override
    public Page<ServerEntity> getUserServers(UserEntity user, Pageable pageable) {
        return serverUserRepository.findUserServers(user, pageable);
    }
}
