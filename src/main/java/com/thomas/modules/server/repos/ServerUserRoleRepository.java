package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerUserRoleEntity;
import com.thomas.modules.server.entity.key.ServerUserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerUserRoleRepository extends JpaRepository<ServerUserRoleEntity, ServerUserRoleKey> {
}
