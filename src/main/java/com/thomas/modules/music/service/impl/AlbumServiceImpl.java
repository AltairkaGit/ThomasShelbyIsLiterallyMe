package com.thomas.modules.music.service.impl;

import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.music.entity.AlbumEntity;
import com.thomas.modules.music.entity.BandEntity;
import com.thomas.modules.music.entity.TrackEntity;
import com.thomas.modules.music.repos.AlbumRepository;
import com.thomas.modules.music.repos.GenreRepository;
import com.thomas.modules.music.repos.TrackRepository;
import com.thomas.modules.music.service.AlbumService;
import com.thomas.modules.music.service.BandService;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {
    private final BandService bandService;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private  final GenreRepository genreRepository;

    public AlbumServiceImpl(BandService bandService, AlbumRepository albumRepository, TrackRepository trackRepository, GenreRepository genreRepository) {
        this.bandService = bandService;
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public AlbumEntity getById(Long albumId) {
        return albumRepository.findById(albumId).get();
    }

    @Override
    public TrackEntity create(BandEntity band, AlbumEntity album, String name, FileEntity file, String genre, FileEntity picture) {
        TrackEntity track = new TrackEntity();

        track.setAlbum(album);
        track.setName(name);
        track.setGenre(genreRepository.findByGenre(genre));
        track.setTrack(file);
        track.setPicture(picture);
        track.setDuration(177);

        trackRepository.saveAndFlush(track);
        return track;
    }

    @Override
    public AlbumEntity create(UserEntity artist, String name, FileEntity picture) throws AuthenticationException {
        BandEntity band = bandService.getUserBandIfOwner(artist.getUserId());
        AlbumEntity album = new AlbumEntity();

        album.setName(name);
        album.setPublished(Timestamp.from(Instant.now()));
        album.setBand(band);
        album.setPicture(picture);

        albumRepository.saveAndFlush(album);
        return album;
    }

    @Override
    public List<AlbumEntity> getLatestReleases() {
        return albumRepository.findAllByOrderByPublishedDesc();
    }

    @Override
    public boolean checkAlbumBelongsGroup(BandEntity band, AlbumEntity album) {
        return Objects.equals(band, album.getBand());
    }
}
