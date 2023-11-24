package com.thomas.modules.server.entity;

import com.thomas.modules.server.entity.key.ServerUserRoleKey;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_user_role")
public class ServerUserRoleEntity {
    @EmbeddedId
    private ServerUserRoleKey id;

    public ServerUserRoleKey getId() {
        return id;
    }

    public void setId(ServerUserRoleKey id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserRoleEntity that = (ServerUserRoleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
