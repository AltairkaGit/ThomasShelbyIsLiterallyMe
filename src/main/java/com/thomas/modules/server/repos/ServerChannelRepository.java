package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerChannelRepository extends JpaRepository<ServerChannelEntity, Long> {
}
