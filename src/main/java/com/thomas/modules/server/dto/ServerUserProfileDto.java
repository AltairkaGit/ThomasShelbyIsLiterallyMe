package com.thomas.modules.server.dto;

import com.thomas.modules.user.dto.UserProfileResponseDto;

public class ServerUserProfileDto extends UserProfileResponseDto {
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
