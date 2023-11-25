package com.thomas.modules.auth.controller;

import com.thomas.modules.auth.dto.AuthRequestDto;
import com.thomas.modules.auth.dto.CheckUsernameIsFreeDto;
import com.thomas.modules.login.dto.TokensResponseDto;
import com.thomas.modules.auth.dto.mapper.AuthRequestMapper;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.auth.service.AuthService;
import com.thomas.modules.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialException;

@RestController
@RequestMapping("api/v2/auth")
public class AuthRestControllerV2 {
    private final AuthService authService;
    private final AuthRequestMapper authRequestMapper;
    private final LoginService loginService;
    @Autowired
    public AuthRestControllerV2(AuthService authService, AuthRequestMapper authRequestMapper, LoginService loginService) {
        this.authService = authService;
        this.authRequestMapper = authRequestMapper;
        this.loginService = loginService;
    }

    @PostMapping("")
    @Operation(summary = "creates a new user with credentials, sends refresh and access token if ok, 400 otherwise")
    public ResponseEntity<TokensResponseDto> createUser(@RequestBody AuthRequestDto authRequestDto) throws CredentialException {
        UserEntity registeringUser = authRequestMapper.convert(authRequestDto);
        registeringUser.setGender(UserEntity.Gender.male);
        UserEntity user = authService.registerUser(registeringUser);
        TokensResponseDto tokens = loginService.jwtLoginUsernamePassword(user, authRequestDto.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/username")
    @Operation(summary = "check if username is free to take")
    public ResponseEntity<Void> checkUsernameIsFree(@RequestBody CheckUsernameIsFreeDto dto) {
        boolean isFree = authService.checkUsernameIsFree(dto.getUsername());
        if (isFree) return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }



    @ExceptionHandler({CredentialException.class})
    public ResponseEntity<String> handleTakenCredentials(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
