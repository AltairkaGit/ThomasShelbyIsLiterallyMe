package com.thomas.modules.server.entity;

import com.thomas.modules.server.entity.key.ServerRoleActionKey;
import com.thomas.roles.ServerRoleAction;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_role_action")
public class ServerRoleActionEntity {
    @EmbeddedId
    ServerRoleActionKey id;

    @ManyToOne
    @MapsId("roleId")
    ServerRoleEntity role;

    public ServerRoleActionKey getId() {
        return id;
    }

    public void setId(ServerRoleActionKey id) {
        this.id = id;
    }

    public ServerRoleEntity getRole() {
        return role;
    }

    public void setRole(ServerRoleEntity role) {
        this.role = role;
    }

    public ServerRoleAction getRoleAction() {
        return ServerRoleAction.valueOf(id.getRoleAction());
    }

    public void setRoleAction(ServerRoleAction roleAction) {
        this.id.setRoleAction(roleAction.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerRoleActionEntity that = (ServerRoleActionEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
