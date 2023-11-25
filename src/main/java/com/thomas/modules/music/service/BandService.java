package com.thomas.modules.music.service;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.music.dto.CreateBandDto;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.user.entity.UserEntity;

import javax.naming.AuthenticationException;
import java.util.Optional;

public interface BandService {
    Optional<BandEntity> getUserBand(Long userId);
    BandEntity getUserBandIfOwner(Long userId) throws AuthenticationException;
    BandEntity createBand(UserEntity owner, CreateBandDto dto);
    void uploadBandPicture(BandEntity band, FileEntity picture);
    void addMember(BandEntity band, Long newArtistId);
}
