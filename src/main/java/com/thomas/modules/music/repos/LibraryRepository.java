package com.thomas.modules.music.repos;

import com.thomas.modules.music.entity.LibraryEntity;
import com.thomas.modules.music.entity.key.LibraryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryRepository extends JpaRepository<LibraryEntity, LibraryKey> {
}
