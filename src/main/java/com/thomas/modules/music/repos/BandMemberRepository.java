package com.thomas.modules.music.repos;

import com.thomas.modules.music.entity.BandMemberEntity;
import com.thomas.modules.music.entity.key.BandMemberKey;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BandMemberRepository extends JpaRepository<BandMemberEntity, BandMemberKey> {
    @Query("SELECT bm.artist FROM BandMemberEntity bm WHERE bm.band.bandId = :bandId")
    List<UserEntity> findAllBandArtists(@Param("bandId") Long bandId);
}
