package com.thomas.modules.music.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class BandMemberKey implements Serializable {
    public static BandMemberKey valueOf(Long artistId, Long bandId) {
        BandMemberKey key = new BandMemberKey();

        key.setArtistId(artistId);
        key.setBandId(bandId);

        return key;
    }

    @Column(name = "artist_id")
    private Long artistId;
    @Column(name = "band_id")
    private Long bandId;

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BandMemberKey that = (BandMemberKey) o;
        return Objects.equals(artistId, that.artistId) && Objects.equals(bandId, that.bandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, bandId);
    }
}
