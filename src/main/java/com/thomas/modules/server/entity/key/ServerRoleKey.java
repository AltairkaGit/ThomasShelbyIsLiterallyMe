package com.thomas.modules.server.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ServerRoleKey implements Serializable {
    public static ServerRoleKey valueOf(Long serverId, String role) {
        ServerRoleKey res = new ServerRoleKey();
        res.setServerId(serverId);
        res.setRole(role);
        return res;
    }
    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "role_name")
    private String role;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerRoleKey that = (ServerRoleKey) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, role);
    }
}
