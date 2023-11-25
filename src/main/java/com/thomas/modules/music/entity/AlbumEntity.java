package com.thomas.modules.music.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thomas.modules.file.entity.FileEntity;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "album")
public class AlbumEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Long albumId;

    @Column(name = "album_name")
    private String name;

    @OneToOne
    @JoinColumn(name = "picture_id")
    private FileEntity picture;

    @OneToOne
    @JoinColumn(name = "band_id")
    private BandEntity band;

    @Column(name = "published")
    private Timestamp published;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album")
    @JsonBackReference
    private List<TrackEntity> tracks;

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileEntity getPicture() {
        return picture;
    }

    public void setPicture(FileEntity picture) {
        this.picture = picture;
    }

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }

    public Timestamp getPublished() {
        return published;
    }

    public void setPublished(Timestamp published) {
        this.published = published;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlbumEntity that = (AlbumEntity) o;
        return Objects.equals(albumId, that.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId);
    }
}
