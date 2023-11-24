package com.thomas.modules.server.service;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface ServerService {
    ServerEntity createServer(@NotNull UserEntity creator, @NotNull String serverName);
    void updatePicture(ServerEntity server, FileEntity picture);
    boolean checkIfServerUser(UserEntity user, Long serverId);
    Optional<ServerEntity> getServerById(Long serverId);
}
