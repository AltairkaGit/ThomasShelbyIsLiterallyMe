package com.thomas.modules.server.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ServerUserKey implements Serializable {
    public static ServerUserKey valueOf(Long userId, Long serverId) {
        ServerUserKey res = new ServerUserKey();
        res.setServerId(serverId);
        res.setUserId(userId);
        return res;
    }

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "server_id")
    private Long serverId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserKey that = (ServerUserKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(serverId, that.serverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, serverId);
    }
}
