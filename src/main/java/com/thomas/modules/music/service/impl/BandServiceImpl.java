package com.thomas.modules.music.service.impl;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.music.dto.CreateBandDto;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.entity.BandMemberEntity;
import com.thomas.modules.music.entity.key.BandMemberKey;
import com.thomas.modules.music.repos.BandMemberRepository;
import com.thomas.modules.music.repos.BandRepository;
import com.thomas.modules.music.service.BandService;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class BandServiceImpl implements BandService {
    private final BandRepository bandRepository;
    private final BandMemberRepository bandMemerRepository;


    public BandServiceImpl(BandRepository bandRepository, BandMemberRepository bandMemerRepository) {
        this.bandRepository = bandRepository;
        this.bandMemerRepository = bandMemerRepository;
    }

    @Override
    public BandEntity getUserBandIfOwner(Long userId) throws AuthenticationException {
        Optional<BandEntity> band = bandRepository.findByOwnerUserId(userId);
        if (band.isEmpty()) throw new AuthenticationException("you are not the owner of the group");
        return band.get();
    }

    @Override
    public BandEntity createBand(UserEntity owner, CreateBandDto dto) {
        BandEntity band = new BandEntity();
        band.setOwner(owner);
        band.setName(dto.getBandName());
        bandRepository.saveAndFlush(band);

        BandMemberEntity member = new BandMemberEntity();
        member.setId(BandMemberKey.valueOf(owner.getUserId(), band.getBandId()));
        bandMemerRepository.saveAndFlush(member);

        return band;
    }

    @Override
    public void uploadBandPicture(BandEntity band, FileEntity picture) {
        band.setPicture(picture);
        bandRepository.saveAndFlush(band);
    }

    @Override
    public void addMember(BandEntity band, Long newArtistId) {
        BandMemberEntity member = new BandMemberEntity();
        member.setId(BandMemberKey.valueOf(newArtistId, band.getBandId()));
        bandMemerRepository.saveAndFlush(member);
    }
}
