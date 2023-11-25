package com.thomas.modules.music.entity;

import com.thomas.modules.music.entity.key.BandMemberKey;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "band_member")
public class BandMemberEntity {
    @EmbeddedId
    private BandMemberKey id;

    @OneToOne
    @JoinColumn(name = "artist_id")
    @MapsId("artistId")
    private UserEntity artist;

    @OneToOne
    @JoinColumn(name = "band_id")
    @MapsId("bandId")
    private BandEntity band;

    public BandMemberKey getId() {
        return id;
    }

    public void setId(BandMemberKey id) {
        this.id = id;
    }

    public UserEntity getArtist() {
        return artist;
    }

    public void setArtist(UserEntity artist) {
        this.artist = artist;
    }

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BandMemberEntity that = (BandMemberEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
