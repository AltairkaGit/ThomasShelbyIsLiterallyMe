package com.thomas.modules.server.repos;

import com.thomas.modules.server.entity.ServerRoleActionEntity;
import com.thomas.modules.server.entity.key.ServerRoleActionKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRoleActionRepository extends JpaRepository<ServerRoleActionEntity, ServerRoleActionKey> {

}
