package com.thomas.modules.music.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.music.dto.TrackDto;
import com.thomas.modules.music.entity.TrackEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", uses={FileResponseMapper.class})
public interface TrackMapper {
    @Mapping(target = "bandName", source = "track.album.band.name")
    @Mapping(target = "albumId", source = "track.album.albumId")
    @Mapping(target = "trackUrl", source = "track.track", qualifiedByName = "getFileUrl")
    @Mapping(target = "clipUrl", source = "track.clip", qualifiedByName = "getFileUrl")
    @Mapping(target = "pictureUrl", source = "track.picture", qualifiedByName = "getFileUrl")
    @Mapping(target = "genre", source = "track.genre.genre")
    TrackDto convert(TrackEntity track);
    List<TrackDto> convertList(List<TrackEntity> tracks);
    default Page<TrackDto> convertPage(Page<TrackEntity> page) {
        List<TrackDto> list = convertList(page.getContent());
        return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
    }

    default Long map(Timestamp timestamp) {
        return timestamp.getTime();
    }
}
