package com.thomas.modules.server.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ServerUserRoleKey implements Serializable {
    public static ServerUserRoleKey valueOf(Long serverId, String role, Long userId) {
        ServerUserRoleKey res = new ServerUserRoleKey();
        res.setServerId(serverId);
        res.setRole(role);
        res.setUserId(userId);
        return res;
    }
    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "role_name")
    private String role;

    @Column(name = "user_id")
    private Long userId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserRoleKey that = (ServerUserRoleKey) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(role, that.role) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, role, userId);
    }
}
