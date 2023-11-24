package com.thomas.modules.security.jwt.impl;

import com.thomas.modules.appRole.entity.AppRoleEntity;
import com.thomas.modules.security.entity.RefreshTokenEntity;
import com.thomas.modules.security.jwt.JwtTokenProvider;
import com.thomas.modules.security.repos.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import javax.naming.AuthenticationException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("${jwt.token.secret}")
    private String secret;
    private SecretKey key;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtTokenProviderImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    private void postConstruct() {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    @Override
    public String generateAccess(String refresh) throws AuthenticationException {
        Jws<Claims> refreshClaims = getClaims(refresh);
        Claims claims = Jwts.claims().setId(refreshClaims.getBody().getId());
        Optional<RefreshTokenEntity> entity = refreshTokenRepository.findByRefresh(refresh);
        claims.put("TokenId", entity.get().getTokenId().toString());
        Date now = new Date();
        Date expired = new Date(now.getTime() + EXPIRED_ACCESS_MS);
        Date refreshExpired = getExpiration(getClaims(refresh));
        expired = expired.before(refreshExpired) ? expired : refreshExpired;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(key)
                .compact();
    }

    @Override
    public String generateRefresh(Long userId, List<AppRoleEntity> roles) {
        Claims claims = Jwts.claims().setId(userId.toString());
        claims.put("AppRoles", mapAppRolesToStrings(roles));
        Date now = new Date();
        Date expired = new Date(now.getTime() + EXPIRED_REFRESH_MS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(key)
                .compact();
    }

    @Override
    public RefreshTokenEntity createRefresh(Long userId, String refresh) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setRefresh(refresh);
        refreshTokenEntity.setUserId(userId);
        refreshTokenRepository.saveAndFlush(refreshTokenEntity);
        return  refreshTokenEntity;
    }

    @Override
    public boolean validateAccess(String access) throws AuthenticationException {
        String refresh = getRefreshFromAccess(access).get().getRefresh();
        return validateRefresh(refresh);
    }

    @Override
    public boolean validateRefresh(String refresh) throws AuthenticationException {
        String lookup = refresh.length() > 500 ? refresh.substring(0, 500) : refresh;
        Optional<RefreshTokenEntity> refreshTokenEntity = refreshTokenRepository.findByRefresh(lookup);
        if (refreshTokenEntity.isEmpty()) return false;
        Jws<Claims> claims = getClaims(refresh);
        long refreshTokenEntityUserId = refreshTokenEntity.get().getUserId();
        long tokenUserId = Long.parseLong(claims.getBody().getId());
        return refreshTokenEntityUserId == tokenUserId;
    }

    @Override
    public Jws<Claims> getClaims(String token) throws AuthenticationException {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        }
        catch (Exception ex) {
            throw new AuthenticationException(ex.getMessage());
        }

    }

    @Override
    public Optional<RefreshTokenEntity> getRefreshFromAccess(String access) throws AuthenticationException {
        Jws<Claims> claims = getClaims(access);
        return refreshTokenRepository.findByTokenId(getTokenId(claims));
    }

    @Override
    public String getAppRolesFromRefresh(Jws<Claims> claims) {
        return (String)claims.getBody().get("AppRoles");
    }

    @Override
    public String getAppRolesFromAccess(Jws<Claims> claims) throws AuthenticationException {
        Jws<Claims> refreshClaims = getRefreshClaimsFromAccessClaims(claims);
        return getAppRolesFromRefresh(refreshClaims);
    }

    @Override
    public Date getExpiration(String token) throws AuthenticationException {
        return getExpiration(getClaims(token));
    }

    @Override
    public Long getUserId(String token) throws AuthenticationException {
        return getUserId(getClaims(token));
    }

    @Override
    public Long getTokenId(String access) throws AuthenticationException {
        return getTokenId(getClaims(access));
    }

    private Long getUserId(Jws<Claims> claims) {
        String id = claims.getBody().getId();
        return Long.parseLong(id);
    }

    private Date getExpiration(Jws<Claims> claims) {
        return claims.getBody().getExpiration();
    }

    private Long getTokenId(Jws<Claims> claims) {
        String tokenId = String.valueOf(claims.getBody().get("TokenId"));
        double d = Double.parseDouble(tokenId);
        return (long)d;
    }

    private static List<String> mapAppRolesToStrings (List<AppRoleEntity> roles) {
        return roles.stream().map(AppRoleEntity::getRole).collect(Collectors.toList());
    }

    private Jws<Claims> getRefreshClaimsFromAccessClaims(Jws<Claims> claims) throws AuthenticationException {
        String refresh = refreshTokenRepository.findByTokenId(getTokenId(claims)).get().getRefresh();
        return getClaims(refresh);
    }


}
