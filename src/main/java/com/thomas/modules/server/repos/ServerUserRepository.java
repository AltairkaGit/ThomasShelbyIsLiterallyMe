package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.server.entity.ServerUserEntity;
import com.thomas.modules.server.entity.key.ServerUserKey;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface ServerUserRepository extends JpaRepository<ServerUserEntity, ServerUserKey> {
    @Modifying
    @Query(value = "INSERT INTO convy.server_user(server_id, user_id) VALUES(:serverId, :userId)", nativeQuery = true)
    void createServerUser(@Param("serverId") long serverId, @Param("userId") long userId);
    Optional<ServerUserEntity> findByServerServerIdAndUserUserId(Long serverId, Long userId);
    @Query("SELECT su FROM ServerUserEntity su WHERE su.server = :server")
    Page<ServerUserEntity> findServerUsers(@Param("server") ServerEntity server, Pageable pageable);
    @Query("SELECT su.server FROM ServerUserEntity su WHERE su.user = :user")
    Page<ServerEntity> findUserServers(@Param("user") UserEntity user, Pageable pageable);
    void deleteAllByUserUserIdInAndServerServerId(Collection<Long> ids, long serverId);
}
