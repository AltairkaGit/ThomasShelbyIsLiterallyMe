package com.thomas.modules.login.dto;

public class TokensResponseDto {
    String access;
    String refresh;

    public TokensResponseDto() {
    }

    public TokensResponseDto(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}
