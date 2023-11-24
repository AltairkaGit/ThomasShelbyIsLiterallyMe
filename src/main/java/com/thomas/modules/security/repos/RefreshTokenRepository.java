package com.thomas.modules.security.repos;

import com.thomas.modules.security.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefresh(String refreshToken);
    Optional<RefreshTokenEntity> findByTokenId(Long tokenId);
    void deleteAllByUserId(Long userId);
}
