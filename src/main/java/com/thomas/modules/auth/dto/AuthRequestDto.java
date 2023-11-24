package com.thomas.modules.auth.dto;

import com.thomas.modules.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;


public class AuthRequestDto {
    private String username;
    private String email;
    @Schema
    private UserEntity.Gender gender;
    private String password;
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public UserEntity.Gender getGender() {
        return gender;
    }
    public String getPassword() {
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setGender(String gender) {
        this.gender = UserEntity.Gender.valueOf(gender);
    }
    public void setGender(UserEntity.Gender gender) { this.gender = gender; }
    public void setPassword(String password) {
        this.password = password;
    }
}
