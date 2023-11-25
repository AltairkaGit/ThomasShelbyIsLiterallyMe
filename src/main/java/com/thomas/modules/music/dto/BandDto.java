package com.thomas.modules.music.dto;

import com.thomas.modules.user.dto.UserProfileResponseDto;

import java.util.List;

public class BandDto {
    private Long bandId;
    private String name;
    private String pictureUrl;
    private Long ownerId;
    private List<UserProfileResponseDto> members;

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<UserProfileResponseDto> getMembers() {
        return members;
    }

    public void setMembers(List<UserProfileResponseDto> members) {
        this.members = members;
    }
}
