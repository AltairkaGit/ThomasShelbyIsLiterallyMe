package com.thomas.modules.music.service;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.music.entity.AlbumEntity;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.entity.TrackEntity;
import com.thomas.modules.user.entity.UserEntity;

import javax.naming.AuthenticationException;
import java.util.List;

public interface AlbumService {
    TrackEntity create(BandEntity band, AlbumEntity album, String name, FileEntity file, String genre, FileEntity picture);
    AlbumEntity create(UserEntity artist, String name, FileEntity picture) throws AuthenticationException;
    AlbumEntity getById(Long albumId);
    List<AlbumEntity> getLatestReleases();
    boolean checkAlbumBelongsGroup(BandEntity band, AlbumEntity album);
}
