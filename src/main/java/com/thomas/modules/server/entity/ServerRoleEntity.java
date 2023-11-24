package com.thomas.modules.server.entity;

import com.thomas.modules.server.entity.key.ServerRoleKey;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_role")
public class ServerRoleEntity {
    @EmbeddedId
    private ServerRoleKey id;

    @ManyToOne
    @MapsId("serverId")
    private ServerEntity server;

    public ServerRoleKey getId() {
        return id;
    }

    public void setId(ServerRoleKey id) {
        this.id = id;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    public String getRole() {
        return id.getRole();
    }

    public void setRoleName(String role) {
        this.id.setRole(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerRoleEntity that = (ServerRoleEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
