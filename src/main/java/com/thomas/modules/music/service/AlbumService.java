package com.thomas.modules.music.service;

import com.thomas.modules.music.entity.AlbumEntity;
import com.thomas.modules.music.entity.BandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumService {
    Page<AlbumEntity> getLatestReleases(Pageable pageable);
    boolean checkAlbumBelongsGroup(BandEntity band, AlbumEntity album);
}
