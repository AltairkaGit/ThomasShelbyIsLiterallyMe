package com.thomas.modules.music.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LibraryKey implements Serializable {
    public static LibraryKey valueOf(Long userId, Long trackId) {
        LibraryKey key = new LibraryKey();

        key.setUserId(userId);
        key.setTrackId(trackId);

        return key;
    }

    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "user_id")
    private Long userId;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
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
        LibraryKey that = (LibraryKey) o;
        return Objects.equals(trackId, that.trackId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId, userId);
    }
}
