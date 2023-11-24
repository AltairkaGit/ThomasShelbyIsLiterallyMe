package com.thomas.modules.file.controller;

import com.thomas.modules.file.dto.FileResponseDto;
import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v2/file")
public class FileRestControllerV2 {
    private final FileResponseMapper fileResponseMapper;
    private final FileService fileService;
    @Autowired
    public FileRestControllerV2(FileResponseMapper fileResponseMapper, FileService fileService) {
        this.fileResponseMapper = fileResponseMapper;
        this.fileService = fileService;
    }

    @PostMapping("")
    @Operation(summary = "upload a multipart file, 200 and FileResponseDto if ok, 400, 500 otherwise")
    public ResponseEntity<FileResponseDto> upload(
            @RequestPart("file") MultipartFile file
    ) {
        FileEntity entity = fileService.uploadFile(file);
        FileResponseDto dto = fileResponseMapper.convert(entity);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
