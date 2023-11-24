package com.thomas.modules.server.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.server.dto.ServerUserProfileDto;
import com.thomas.modules.server.entity.ServerUserEntity;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = { UserProfileResponseMapper.class, FileResponseMapper.class })
public interface ServerUserMapper {
    @Mapping(target = "userId", source = "serverUser.user.userId")
    @Mapping(target = "username", source = "serverUser.user.username")
    @Mapping(target = "profilePictureUrl", source = "serverUser.user.profilePicture", qualifiedByName = "getFileUrl")
    ServerUserProfileDto convert(ServerUserEntity serverUser);

    List<ServerUserProfileDto> convertList(Collection<ServerUserEntity> list);

    default Page<ServerUserProfileDto> convertPage(Page<ServerUserEntity> page) {
        List<ServerUserProfileDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }
}
