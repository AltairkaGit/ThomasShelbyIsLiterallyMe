package com.thomas.modules.music.repos;

import com.thomas.modules.music.entity.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, String> {
    GenreEntity findByGenre(String genre);
}
