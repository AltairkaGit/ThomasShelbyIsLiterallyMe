package com.thomas.modules.music.entity;

import com.thomas.modules.music.entity.key.LibraryKey;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "library")
public class LibraryEntity {
    @EmbeddedId
    private LibraryKey id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @MapsId("userId")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "track_id")
    @MapsId("trackId")
    private TrackEntity track;

    public LibraryKey getId() {
        return id;
    }

    public void setId(LibraryKey id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(TrackEntity track) {
        this.track = track;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryEntity that = (LibraryEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
