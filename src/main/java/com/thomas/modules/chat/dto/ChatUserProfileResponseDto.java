package com.thomas.modules.chat.dto;

import com.thomas.modules.user.dto.UserProfileResponseDto;
import com.thomas.roles.ChatRole;

import java.util.List;

public class ChatUserProfileResponseDto {
    private List<ChatRole> roles;
    private UserProfileResponseDto profile;

    public List<ChatRole> getRoles() {
        return roles;
    }

    public void setRoles(List<ChatRole> roles) {
        this.roles = roles;
    }

    public UserProfileResponseDto getProfile() {
        return profile;
    }

    public void setProfile(UserProfileResponseDto profile) {
        this.profile = profile;
    }
}
