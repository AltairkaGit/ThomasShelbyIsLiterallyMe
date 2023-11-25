package com.thomas.modules.music.repos;

import com.thomas.modules.music.entity.BandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BandRepository extends JpaRepository<BandEntity, Long> {
    Optional<BandEntity> findByOwnerUserId(Long ownerId);

}
