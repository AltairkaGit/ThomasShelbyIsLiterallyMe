package com.thomas.modules.music.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thomas.modules.file.entity.FileEntity;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "track")
public class TrackEntity {
    @Id
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "track_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    @OneToOne
    @JoinColumn(name = "track_id")
    private FileEntity track;

    @OneToOne
    @JoinColumn(name = "clip_id")
    private FileEntity clip;

    @OneToOne
    @JoinColumn(name = "picture_id")
    private FileEntity picture;

    @Column(name = "diration")
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "genre")
    private GenreEntity genre;

    @OneToMany(fetch =  FetchType.LAZY, mappedBy = "track")
    @JsonBackReference
    private Set<LibraryEntity> likedUsers;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AlbumEntity getAlbum() {
        return album;
    }

    public void setAlbum(AlbumEntity album) {
        this.album = album;
    }

    public FileEntity getTrack() {
        return track;
    }

    public void setTrack(FileEntity track) {
        this.track = track;
    }

    public FileEntity getClip() {
        return clip;
    }

    public void setClip(FileEntity clip) {
        this.clip = clip;
    }

    public FileEntity getPicture() {
        return picture;
    }

    public void setPicture(FileEntity picture) {
        this.picture = picture;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreEntity genre) {
        this.genre = genre;
    }

    public Set<LibraryEntity> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(Set<LibraryEntity> likedUsers) {
        this.likedUsers = likedUsers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackEntity that = (TrackEntity) o;
        return Objects.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId);
    }
}
