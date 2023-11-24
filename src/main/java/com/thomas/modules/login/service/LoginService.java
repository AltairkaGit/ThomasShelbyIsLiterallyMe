package com.thomas.modules.login.service;

import com.thomas.modules.login.dto.TokensResponseDto;
import com.thomas.modules.user.entity.UserEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;

@Transactional
public interface LoginService {
    TokensResponseDto jwtLoginUsernamePassword(UserEntity user, String rawPassword) throws CredentialException;
    boolean checkPassword(UserEntity user, String rawPassword);
    TokensResponseDto refreshToken(String refresh) throws AuthenticationException;
    void logoutSession(String access) throws AuthenticationException;
    void logoutAllSessions(String access) throws AuthenticationException;
    HttpHeaders setJwtCookieInHeaders(HttpHeaders old, String token);
}
