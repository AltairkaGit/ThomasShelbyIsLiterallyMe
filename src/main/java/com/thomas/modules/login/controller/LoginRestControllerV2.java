package com.thomas.modules.login.controller;

import com.thomas.modules.login.dto.LoginRequestDto;
import com.thomas.modules.login.dto.RefreshTokenDto;
import com.thomas.modules.login.dto.TokensResponseDto;
import com.thomas.modules.login.service.LoginService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.security.auth.login.CredentialException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v2")
public class LoginRestControllerV2 {
    private final LoginService loginService;
    private final UserService userService;

    public LoginRestControllerV2(LoginService loginService, UserService userService) {
        this.loginService = loginService;
        this.userService = userService;
    }

    /**
     *
     * @param loginRequestDto username and password
     * @return code 200 and access, refresh tokens if ok
     * @throws CredentialException returns 401 if login or password is incorrect
     */
    @PostMapping(value="/login")
    @Operation(summary = "[Public url] username and password login, returns access and refresh token if ok, 401, 500 otherwise")
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) throws CredentialException {
        UserEntity user = userService.getUserByUsername(loginRequestDto.getUsername());
        return ResponseEntity.ok(loginService.jwtLoginUsernamePassword(user, loginRequestDto.getPassword()));
    }

    /**
     *
     * @param access access token from filter
     * @return code 200 if successful logout, 401 or 500 otherwise
     */
    @Operation(summary = "logout the session on a specific device if ok, 401, 500 otherwise")
    @GetMapping(value="/logout")
    public ResponseEntity<Void> logoutSession(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutSession(access);
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param access access token from filter
     * @return code 200 if successful logout all sessions, 401 or 500 otherwise
     */
    @Operation(summary = "logout all the sessions on all your devices if ok, 401, 500 otherwise")
    @GetMapping(value="/logout-all-sessions")
    public ResponseEntity<Void> logoutAllSessions(@RequestAttribute("accessToken") String access) throws AuthenticationException {
        loginService.logoutAllSessions(access);
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @return 200 if token is ok, 401 or 500 otherwise
     */
    @GetMapping(value="/verify-access")
    @Operation(summary = "return 200 if token if ok, 401, 500 otherwise")
    public ResponseEntity<Void> verifyAccess() {
        return ResponseEntity.ok().build();
    }

    /**
     *
     * @param dto refresh token dto
     * @return code 200 and new tokens, 401 or 500 otherwise
     */
    @PostMapping(value="/refresh")
    @Operation(summary = "[Public url] return 200 and two tokens if ok, 401, 500 otherwise")
    public ResponseEntity<TokensResponseDto> refreshAccessToken(@RequestBody RefreshTokenDto dto) throws AuthenticationException {
        TokensResponseDto tokens = loginService.refreshToken(dto.getRefresh());
        return ResponseEntity.ok(tokens);
    }

    @ExceptionHandler({
        NoSuchElementException.class,
        HttpMessageNotReadableException.class,
    })
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
