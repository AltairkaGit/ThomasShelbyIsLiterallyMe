package com.thomas.modules.music.dto.mapper;

import com.thomas.modules.chat.dto.RoomMessageDto;
import com.thomas.modules.file.dto.mapper.FileResponseMapper;
import com.thomas.modules.music.dto.RoomDto;
import com.thomas.modules.music.model.Room;
import com.thomas.modules.music.model.RoomMessage;
import com.thomas.modules.music.repos.TrackRepository;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.user.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public abstract class RoomMapper {
    @Autowired
    public TrackMapper trackMapper;
    @Autowired
    public TrackRepository trackRepository;
    @Autowired
    public UserService userService;
    @Autowired
    public FileResponseMapper fileResponseMapper;
    public RoomDto convert(Long userId, Room room) {
        RoomDto dto = new RoomDto();
        dto.setOwnerId(room.getOwnerId());
        dto.setUsers(room.getUsers());
        dto.setOwner(Objects.equals(userId, dto.getOwnerId()));
        dto.setTracks(trackMapper.convertList(trackRepository.findAllTracksByTrackIdIn(room.getTrackList())));
        dto.setMessages(convertList(room.getMessageList()));
        return dto;

    }

    public List<RoomMessageDto> convertList(List<RoomMessage> messages) {
        return messages.stream().map(this::convertMessage).collect(Collectors.toList());
    }

    public RoomMessageDto convertMessage(RoomMessage message) {
        RoomMessageDto dto = new RoomMessageDto();
        UserEntity sender = userService.getUserById(message.getSenderId());
        dto.setContent(message.getContent());
        dto.setSenderId(message.getSenderId());
        dto.setSendTimestamp(message.getSendTimestamp().getTime());
        dto.setSenderPictureUrl(fileResponseMapper.map(sender.getProfilePicture()));
        return dto;
    }
}
