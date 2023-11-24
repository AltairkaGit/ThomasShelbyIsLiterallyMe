package com.thomas.modules.server.entity;

import com.thomas.modules.server.entity.key.ServerUserKey;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "server_user")
public class ServerUserEntity {
    @EmbeddedId
    private ServerUserKey id;
    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private UserEntity user;
    @ManyToOne
    @MapsId("server_id")
    @JoinColumn(name = "server_id")
    private ServerEntity server;

    public ServerUserKey getId() {
        return id;
    }

    public void setId(ServerUserKey id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ServerEntity getServer() {
        return server;
    }

    public void setServer(ServerEntity server) {
        this.server = server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUserEntity that = (ServerUserEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
