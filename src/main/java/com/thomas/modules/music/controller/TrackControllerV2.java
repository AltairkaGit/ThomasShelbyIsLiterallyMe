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
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

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

    public TrackControllerV2(UserService userService, AlbumService albumService, BandService bandService, AlbumMapper albumMapper, TrackMapper trackMapper, FileService fileService, TrackRepository trackRepository) {
        this.userService = userService;
        this.albumService = albumService;
        this.bandService = bandService;
        this.albumMapper = albumMapper;
        this.trackMapper = trackMapper;
        this.fileService = fileService;
        this.trackRepository = trackRepository;
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

    @PostMapping("/tracks")
    public ResponseEntity<List<TrackDto>> getTracksByIds(
            @RequestBody TracksRequestDto dto
    ) {
        List<TrackEntity> tracks = trackRepository.findAllTracksByTrackIdIn(dto.getIds());
        return ResponseEntity.ok(trackMapper.convertList(tracks));
    }
}
