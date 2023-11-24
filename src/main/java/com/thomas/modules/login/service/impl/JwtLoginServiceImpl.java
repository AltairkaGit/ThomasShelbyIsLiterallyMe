package com.thomas.modules.login.service.impl;

import com.thomas.modules.login.dto.TokensResponseDto;
import com.thomas.modules.security.entity.RefreshTokenEntity;
import com.thomas.modules.security.repos.RefreshTokenRepository;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.security.jwt.JwtTokenProvider;
import com.thomas.modules.login.service.LoginService;
import com.thomas.modules.user.service.UserService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import java.util.Optional;


@Service
public class JwtLoginServiceImpl implements LoginService {
    private static final String tokenHeader = "Authorization";
    private static final Integer tokenExpires = 365;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    @Autowired
    public JwtLoginServiceImpl(JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, RefreshTokenRepository refreshTokenRepository, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public TokensResponseDto jwtLoginUsernamePassword(UserEntity user, String rawPassword) throws CredentialException {
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) throw new CredentialException("username or password is wrong");
        String refresh = jwtTokenProvider.generateRefresh(user.getUserId(), user.getRoles());
        jwtTokenProvider.createRefresh(user.getUserId(), refresh);
        String access = null;
        try {
            access = jwtTokenProvider.generateAccess(refresh);
        } catch (AuthenticationException ignored) {
        }
        return new TokensResponseDto(access, refresh);
    }

    @Override
    public boolean checkPassword(UserEntity user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public void logoutSession(String access) throws AuthenticationException {
        Optional<RefreshTokenEntity> refresh = jwtTokenProvider.getRefreshFromAccess(access);
        if (refresh.isEmpty()) throw new AuthenticationException("token is not valid anymore");
        refreshTokenRepository.delete(refresh.get());
    }

    @Override
    public void logoutAllSessions(String access) throws AuthenticationException {
        Optional<RefreshTokenEntity> refresh = jwtTokenProvider.getRefreshFromAccess(access);
        if (refresh.isEmpty()) throw new AuthenticationException("token is not valid anymore");
        Long userId = refresh.get().getUserId();
        refreshTokenRepository.deleteAllByUserId(userId);
        refreshTokenRepository.flush();
    }

    @Override
    public TokensResponseDto refreshToken(String refreshOld) throws AuthenticationException {
        Optional<RefreshTokenEntity> token = refreshTokenRepository.findByRefresh(refreshOld);
        if (token.isEmpty()) throw new AuthenticationException("login again, please");
        UserEntity user = userService.getUserById(jwtTokenProvider.getUserId(refreshOld));
        String refresh = jwtTokenProvider.generateRefresh(user.getUserId(), user.getRoles());
        updateRefresh(token.get(), refresh);
        String access = jwtTokenProvider.generateAccess(refresh);
        return new TokensResponseDto(access, refresh);
    }

    @Override
    public HttpHeaders setJwtCookieInHeaders(HttpHeaders old, String token) {
        HttpHeaders headers = new HttpHeaders(old);
        Cookie cookie = new Cookie(tokenHeader, token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(tokenExpires * 24 * 60 * 1000);
        cookie.setPath("/");
        String setCookieHeader =bakeSetCookieHeader(cookie);
        headers.add("Set-Cookie", setCookieHeader);
        return headers;
    }

    private String bakeSetCookieHeader(Cookie cookie) {
        return String.format("%s=%s; Max-Age=%d; Path=%s; HttpOnly;",
                cookie.getName(), cookie.getValue(), cookie.getMaxAge(), cookie.getPath());
    }

    private void updateRefresh(RefreshTokenEntity token, String refresh) {
        token.setRefresh(refresh);
        refreshTokenRepository.saveAndFlush(token);
    }

}
