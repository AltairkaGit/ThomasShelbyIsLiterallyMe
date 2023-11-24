package com.thomas.modules.user.dto;

import com.thomas.modules.user.entity.UserEntity;

public class MyProfileDto {
    private Long userId;
    private String username;
    private String profilePictureUrl;
    private String email;
    private UserEntity.Gender gender;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEntity.Gender getGender() {
        return gender;
    }

    public void setGender(UserEntity.Gender gender) {
        this.gender = gender;
    }
}
