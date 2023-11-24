package com.thomas.modules.file.dto.mapper;

import com.thomas.modules.file.dto.FileResponseDto;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.service.FileService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring", implementationName = "FileResponseMapperImpl")
public abstract class FileResponseMapper implements Converter<FileEntity, FileResponseDto> {
    @Autowired
    FileService fileService;
    public FileResponseDto convert(FileEntity file) {
        FileResponseDto res = new FileResponseDto();
        res.setId(file.getFileId());
        res.setName(file.getName());
        res.setMimeType(file.getMimeType());
        res.setUrl(map(Optional.of(file)));
        return res;
    }

    @Named("getFileUrl")
    public String map(Optional<FileEntity> file) {
        return file.map(fileEntity -> fileService.composeUrl(fileEntity.getName())).orElse("");
    }

    @Named("getFileUrls")
    public List<String> map(List<FileEntity> files) {
        if (files == null) return null;
        return files.stream().map(file -> fileService.composeUrl(file.getName())).collect(Collectors.toList());
    }

}
