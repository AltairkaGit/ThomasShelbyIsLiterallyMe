package com.thomas.modules.music.dto;

import com.thomas.modules.chat.dto.RoomMessageDto;

import java.util.List;

public class RoomDto {

    private Long ownerId;
    private List<TrackDto> tracks;
    private List<RoomMessageDto> messages;
    private List<Long> users;
    private boolean isOwner;
    private String artifact;

    public List<Long> getUsers() {
        return users;
    }

    public void setUsers(List<Long> users) {
        this.users = users;
    }

    public List<TrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDto> tracks) {
        this.tracks = tracks;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public List<RoomMessageDto> getMessages() {
        return messages;
    }

    public void setMessages(List<RoomMessageDto> messages) {
        this.messages = messages;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }
}
