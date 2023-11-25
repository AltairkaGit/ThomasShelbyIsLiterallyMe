package com.thomas.modules.music.repos;

import com.thomas.modules.music.entity.TrackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    List<TrackEntity> findAllByGenreGenre(String genre);
    List<TrackEntity> findAllTracksByTrackIdIn(List<Long> ids);
}
