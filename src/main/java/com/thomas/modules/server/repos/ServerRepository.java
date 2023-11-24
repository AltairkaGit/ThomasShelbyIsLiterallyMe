package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServerRepository extends JpaRepository<ServerEntity, Long> {
}
