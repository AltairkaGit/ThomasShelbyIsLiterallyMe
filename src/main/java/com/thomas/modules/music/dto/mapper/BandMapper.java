package com.thomas.modules.music.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.music.dto.BandDto;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.repos.BandMemberRepository;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import com.thomas.modules.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Mapper(componentModel = "spring")
public abstract class BandMapper {
    @Autowired
    public BandMemberRepository bandMemberRepository;
    @Autowired
    public FileResponseMapper fileResponseMapper;
    @Autowired
    public UserProfileResponseMapper userProfileResponseMapper;

    public BandDto convert(BandEntity entity) {
        BandDto dto = new BandDto();
        List<UserEntity> members = bandMemberRepository.findAllBandArtists(entity.getBandId());

        dto.setName(entity.getName());
        dto.setBandId(entity.getBandId());
        dto.setOwnerId(entity.getOwner().getUserId());
        dto.setPictureUrl(fileResponseMapper.map(Optional.of(entity.getPicture())));
        dto.setMembers(userProfileResponseMapper.convertList(members));

        return dto;
    }
}
