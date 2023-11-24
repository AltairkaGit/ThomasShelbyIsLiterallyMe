package com.thomas.modules.server.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thomas.modules.file.entity.FileEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "server")
public class ServerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @Column(name = "servername", nullable = false)
    private String servername;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    @JsonManagedReference
    private FileEntity serverPicture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "server")
    @JsonBackReference
    private List<ServerChannelEntity> channels;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "server")
    @JsonBackReference
    private Set<ServerUserEntity> serverUsers;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public Optional<FileEntity> getServerPicture() {
        if (serverPicture == null) return Optional.empty();
        return Optional.of(serverPicture);
    }

    public void setServerPicture(FileEntity serverPicture) {
        this.serverPicture = serverPicture;
    }

    public List<ServerChannelEntity> getChannels() {
        return channels;
    }

    public void setChannels(List<ServerChannelEntity> channels) {
        this.channels = channels;
    }

    public Set<ServerUserEntity> getServerUsers() {
        return serverUsers;
    }

    public void setServerUsers(Set<ServerUserEntity> serverUsers) {
        this.serverUsers = serverUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerEntity that = (ServerEntity) o;
        return Objects.equals(serverId, that.serverId) && Objects.equals(servername, that.servername) && Objects.equals(serverPicture, that.serverPicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serverId, servername, serverPicture);
    }
}
