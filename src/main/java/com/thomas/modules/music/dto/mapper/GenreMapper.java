package com.thomas.modules.music.dto.mapper;

import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.music.dto.GenreDto;
import com.thomas.modules.music.entity.GenreEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring", uses = {FileResponseMapper.class})
public interface GenreMapper {
    @Mapping(target = "pictureUrl", source = "genre.picture", qualifiedByName = "getFileUrlWithNull")
    GenreDto convert(GenreEntity genre);

    List<GenreDto> convertList(List<GenreEntity> list);
}
