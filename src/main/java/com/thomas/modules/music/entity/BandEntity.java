package com.thomas.modules.music.entity;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "band")
public class BandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_id")
    private Long bandId;

    @Column(name = "band_name", length = 35)
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picture_id")
    private FileEntity picture;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BandEntity that = (BandEntity) o;
        return Objects.equals(bandId, that.bandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bandId);
    }
}
