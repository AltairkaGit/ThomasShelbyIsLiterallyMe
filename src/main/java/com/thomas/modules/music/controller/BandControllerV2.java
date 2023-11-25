package com.thomas.modules.music.controller;

import com.thomas.lib.page.PageDto;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.music.dto.*;
import com.thomas.modules.music.dto.mapper.BandMapper;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.service.BandService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v2/band")
public class BandControllerV2 {
    private final FileService fileService;
    private final BandService bandService;
    private final BandMapper bandMapper;
    private final UserService userService;

    public BandControllerV2(FileService fileService, BandService bandService, BandMapper bandMapper, UserService userService) {
        this.fileService = fileService;
        this.bandService = bandService;
        this.bandMapper = bandMapper;
        this.userService = userService;
    }

    @PostMapping("")
    ResponseEntity<BandDto> createBand(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody CreateBandDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        BandEntity band = bandService.createBand(me, dto);
        return ResponseEntity.ok(bandMapper.convert(band));
    }

    @PostMapping("/member")
    ResponseEntity<Void> createAlbum(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody AddBandMemberDto dto
    ) throws AuthenticationException {
        BandEntity band = bandService.getUserBandIfOwner(userId);
        bandService.addMember(band, dto.getUserId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/picture")
    @Transactional
    public ResponseEntity<Void> setBandPicture(
            @RequestAttribute("reqUserId") Long userId,
            @RequestPart("file") MultipartFile file
    ) throws AuthenticationException {
        BandEntity band = bandService.getUserBandIfOwner(userId);
        FileEntity picture = fileService.uploadFile(file);
        bandService.uploadBandPicture(band, picture);
        return ResponseEntity.ok().build();
    }


}
