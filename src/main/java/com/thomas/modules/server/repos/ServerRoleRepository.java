package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerRoleEntity;
import com.thomas.modules.server.entity.key.ServerRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRoleRepository extends JpaRepository<ServerRoleEntity, ServerRoleKey> {
}
