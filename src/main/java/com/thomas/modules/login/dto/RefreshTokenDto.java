package com.thomas.modules.login.dto;

public class RefreshTokenDto {
    String refresh;

    public RefreshTokenDto() {
    }

    public RefreshTokenDto(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
