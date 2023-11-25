package com.thomas.modules.music.controller;

import com.thomas.modules.music.dto.GenreDto;
import com.thomas.modules.music.dto.mapper.GenreMapper;
import com.thomas.modules.music.entity.GenreEntity;
import com.thomas.modules.music.repos.GenreRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/genres")
public class GenresControllerV2 {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    public GenresControllerV2(GenreRepository genreRepository, GenreMapper genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<GenreDto>> getGenres() {
        List<GenreEntity> genres = genreRepository.findAll();
        return ResponseEntity.ok(genreMapper.convertList(genres));
    }

    @GetMapping("")
    public ResponseEntity<GenreDto> getGenre(
            @RequestParam("genre") String genre
    ) {
        GenreEntity entity = genreRepository.findByGenre(genre);
        return ResponseEntity.ok(genreMapper.convert(entity));
    }

}
