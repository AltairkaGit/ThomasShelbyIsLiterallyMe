package com.thomas.modules.security.jwt;

import com.thomas.modules.appRole.entity.AppRoleEntity;
import com.thomas.modules.security.entity.RefreshTokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
public interface JwtTokenProvider {
    long EXPIRED_ACCESS_MS = 30L * 24 * 60 * 60 * 1000;
    long EXPIRED_REFRESH_MS = 365L * 24 * 60 * 60 * 1000;
    String generateAccess(String refresh) throws AuthenticationException;
    String generateRefresh(Long userId, List<AppRoleEntity> roles);
    RefreshTokenEntity createRefresh(Long userId, String refresh);
    boolean validateAccess(String access) throws AuthenticationException;
    boolean validateRefresh(String refresh) throws AuthenticationException;
    Jws<Claims> getClaims(String token) throws AuthenticationException;
    Optional<RefreshTokenEntity> getRefreshFromAccess(String access) throws AuthenticationException;
    Date getExpiration(String token) throws AuthenticationException;
    Long getUserId(String token) throws AuthenticationException;
    Long getTokenId(String access) throws AuthenticationException;
    String getAppRolesFromRefresh(Jws<Claims> claims);
    String getAppRolesFromAccess(Jws<Claims> claims) throws AuthenticationException;
}
