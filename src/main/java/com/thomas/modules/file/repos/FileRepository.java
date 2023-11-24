package com.thomas.modules.file.repos;

import com.thomas.modules.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByName(@Param("filename") String filename);
}
