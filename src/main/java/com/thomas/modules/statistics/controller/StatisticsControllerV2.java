package com.thomas.modules.statistics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v2/statistics")
public class StatisticsControllerV2 {

    @GetMapping("/calculateMostListenableArtists")
    public ResponseEntity<List<Long>> getMostListenableArtists() {
        return ResponseEntity.ok(List.of(1L, 2L, 3L));
    }

    @GetMapping("/calculateMostListenableArtistsLastMonth")
    public ResponseEntity<List<Long>> getMostListenableArtistsLastMonth() {
        return ResponseEntity.ok(List.of(1L, 2L));
    }

    @GetMapping("/getTodaysUniqueUsersAmount")
    public ResponseEntity<Integer> getTodaysUniqueUsers() {
        return ResponseEntity.ok(Set.of(1L, 3L, 32L, 14L, 7L, 8L).size());
    }

    @GetMapping("/getMonthlyMostPopularGenres")
    public ResponseEntity<List<String>> getMonthlyMostPopularGenres() {
        return ResponseEntity.ok(List.of("РУССКИЙ_РОК", "КЛАССИКА"));
    }
}
