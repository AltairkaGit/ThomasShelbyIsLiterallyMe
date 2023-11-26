package com.thomas.modules.music.controller;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.file.service.FileService;
import com.thomas.modules.music.dto.AlbumDto;
import com.thomas.modules.music.dto.TrackDto;
import com.thomas.modules.music.dto.TracksRequestDto;
import com.thomas.modules.music.dto.mapper.AlbumMapper;
import com.thomas.modules.music.dto.mapper.TrackMapper;
import com.thomas.modules.music.entity.AlbumEntity;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.entity.TrackEntity;
import com.thomas.modules.music.repos.TrackRepository;
import com.thomas.modules.music.service.AlbumService;
import com.thomas.modules.music.service.BandService;
import com.thomas.modules.music.service.TrackService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/music")
public class TrackControllerV2 {
    private final UserService userService;
    private final AlbumService albumService;
    private final BandService bandService;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;
    private final FileService fileService;
    private final TrackRepository trackRepository;
    private final TrackService trackService;

    public TrackControllerV2(
            UserService userService,
            AlbumService albumService,
            BandService bandService,
            AlbumMapper albumMapper,
            TrackMapper trackMapper,
            FileService fileService,
            TrackRepository trackRepository,
            TrackService trackService
    ) {
        this.userService = userService;
        this.albumService = albumService;
        this.bandService = bandService;
        this.albumMapper = albumMapper;
        this.trackMapper = trackMapper;
        this.fileService = fileService;
        this.trackRepository = trackRepository;
        this.trackService = trackService;
    }

    @GetMapping("/lastReleases")
    public ResponseEntity<List<AlbumDto>> getLastReleases() {
        List<AlbumEntity> lastReleases = albumService.getLatestReleases();
        return ResponseEntity.ok(albumMapper.convertList(lastReleases));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable Long albumId) {
        return ResponseEntity.ok(albumMapper.convert(albumService.getById(albumId)));
    }

    @PostMapping("/album")
    @Transactional
    public ResponseEntity<AlbumDto> createAlbum(
            @RequestAttribute("reqUserId") Long userId,
            @RequestPart("picture") MultipartFile picture,
            @RequestPart("name") String name
    ) throws AuthenticationException {
        BandEntity band = bandService.getUserBandIfOwner(userId);
        UserEntity artist = userService.getUserById(userId);
        FileEntity pictureFile = fileService.uploadFile(picture);
        AlbumEntity album = albumService.create(artist, name, pictureFile);
        return ResponseEntity.ok(albumMapper.convert(album));
    }

    @PostMapping("/album/{albumId}/track")
    @Transactional
    public ResponseEntity<TrackDto> createAlbum(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long albumId,
            @RequestPart("file") MultipartFile file,
            @RequestPart("picture") MultipartFile picture,
            @RequestPart("name") String name,
            @RequestPart("genre") String genre
    ) throws AuthenticationException {
        BandEntity band = bandService.getUserBandIfOwner(userId);
        UserEntity artist = userService.getUserById(userId);
        AlbumEntity album = albumService.getById(albumId);
        if (!albumService.checkAlbumBelongsGroup(band, album))
            throw new AuthenticationException("you can't upload track to this album");
        FileEntity pictureFile = fileService.uploadFile(picture);
        FileEntity trackFile = fileService.uploadFile(file);
        TrackEntity track = albumService.create(band, album, name, trackFile, genre, pictureFile);
        return ResponseEntity.ok(trackMapper.convert(track));
    }

    @GetMapping("/genre")
    public ResponseEntity<List<TrackDto>> getTracksByGenre(
        @RequestParam("genre") String  genre
    ) {
        List<TrackEntity> tracks = trackRepository.findAllByGenreGenre(genre);
        return ResponseEntity.ok(trackMapper.convertList(tracks));
    }

    @GetMapping("/track/{trackId}")
    public ResponseEntity<TrackDto> getTrackById(
            @PathVariable Long trackId
    ) {
        Optional<TrackEntity> track = trackRepository.findById(trackId);
        if (track.isEmpty()) throw new IllegalArgumentException("No track with this id");
        return ResponseEntity.ok(trackMapper.convert(track.get()));
    }

    @Operation(description = "get hls index file of the m3u8")
    @RequestMapping(
            path = "/track/{m3u8Url}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> index(
            @PathVariable String m3u8Url
    ) {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/vnd.apple.mpegurl");
            headers.set("Content-Disposition", "attachment;filename=" + m3u8Url);
            StreamingResponseBody body = trackService.m3u8Index(m3u8Url);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    @PostMapping("/tracks")
    public ResponseEntity<List<TrackDto>> getTracksByIds(
            @RequestBody TracksRequestDto dto
    ) {
        List<TrackEntity> tracks = trackRepository.findAllTracksByTrackIdIn(dto.getIds());
        return ResponseEntity.ok(trackMapper.convertList(tracks));
    }


}
