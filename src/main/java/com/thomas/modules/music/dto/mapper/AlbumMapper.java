package com.thomas.modules.music.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.music.dto.AlbumDto;
import com.thomas.modules.music.entity.AlbumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses={FileResponseMapper.class})
public interface AlbumMapper {
    @Mapping(target = "pictureUrl", source = "album.picture", qualifiedByName = "getFileUrl")
    @Mapping(target = "bandName", source = "album.band.name")
    @Mapping(target = "bandId", source = "album.band.bandId")
    AlbumDto convert(AlbumEntity album);
    List<AlbumDto> convertList(List<AlbumEntity> tracks);
    default Page<AlbumDto> convertPage(Page<AlbumEntity> page) {
        List<AlbumDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    default Long map(Timestamp timestamp) {
        return timestamp.getTime();
    }
}
