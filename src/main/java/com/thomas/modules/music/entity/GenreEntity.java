package com.thomas.modules.music.entity;

import com.thomas.modules.file.entity.FileEntity;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "genre")
public class GenreEntity {
    @Id
    @Column(name = "genre", length = 35)
    private String genre;

    @OneToOne
    @JoinColumn(name = "picture_id")
    private FileEntity picture;

    @ManyToOne
    @JoinColumn(name = "parent", referencedColumnName = "genre", insertable = false, updatable = false)
    private GenreEntity parentGenre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "genre")
    private Set<TrackEntity> tracks;

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public FileEntity getPicture() {
        return picture;
    }

    public void setPicture(FileEntity picture) {
        this.picture = picture;
    }

    public String getParentGenre() {
        if (parentGenre != null) return parentGenre.genre;
        return "";
    }

    public void setParentGenre(GenreEntity parentGenre) {
        this.parentGenre = parentGenre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreEntity that = (GenreEntity) o;
        return Objects.equals(genre, that.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genre);
    }
}
