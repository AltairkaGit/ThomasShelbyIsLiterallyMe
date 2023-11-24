package com.thomas.modules.server.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.server.dto.ServerDto;
import com.thomas.modules.server.dto.ServerExtendedDto;
import com.thomas.modules.server.entity.ServerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", uses={FileResponseMapper.class, ServerChannelMapper.class})
public interface ServerMapper extends Converter<ServerEntity, ServerDto> {
    @Mapping(target = "serverPictureUrl", source = "serverPicture", qualifiedByName = "getFileUrl")
    ServerDto convert(ServerEntity server);
    @Mapping(target = "serverPictureUrl", source = "serverPicture", qualifiedByName = "getFileUrl")
    ServerExtendedDto convertExtended(ServerEntity server);

    default List<ServerDto> convertList(List<ServerEntity> servers) {
        return servers.stream().map(this::convert).collect(Collectors.toList());
    }
    default Page<ServerDto> convertPage(Page<ServerEntity> page) {
        List<ServerDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

}
