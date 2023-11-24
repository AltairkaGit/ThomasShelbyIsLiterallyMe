package com.thomas.modules.server.dto;

public class ServerDto {
    private Long serverId;
    private String servername;
    private String serverPictureUrl;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getServername() {
        return servername;
    }

    public void setServername(String servername) {
        this.servername = servername;
    }

    public String getServerPictureUrl() {
        return serverPictureUrl;
    }

    public void setServerPictureUrl(String serverPictureUrl) {
        this.serverPictureUrl = serverPictureUrl;
    }
}
