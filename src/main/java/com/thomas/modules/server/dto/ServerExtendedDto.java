package com.thomas.modules.server.dto;

import java.util.List;

public class ServerExtendedDto extends ServerDto {
    List<ServerChannelDto> channels;

    public List<ServerChannelDto> getChannels() {
        return channels;
    }

    public void setChannels(List<ServerChannelDto> channels) {
        this.channels = channels;
    }
}
