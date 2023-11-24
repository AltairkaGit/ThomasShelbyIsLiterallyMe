package com.thomas.modules.login.dto;

public class AccessTokenDto {
    String access;

    public AccessTokenDto() {
    }

    public AccessTokenDto(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
