package com.thomas.modules.security.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id", nullable = false)
    private Long tokenId;
    @Column(name = "refresh_token", nullable = false)
    private String refresh;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshTokenEntity that = (RefreshTokenEntity) o;
        return Objects.equals(tokenId, that.tokenId) && Objects.equals(refresh, that.refresh) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId, refresh, userId);
    }
}
