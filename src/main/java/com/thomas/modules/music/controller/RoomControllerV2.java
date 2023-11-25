package com.thomas.modules.music.controller;

import com.thomas.modules.music.dto.RoomDto;
import com.thomas.modules.music.dto.mapper.RoomMapper;
import com.thomas.modules.music.model.Room;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v2/room")
public class RoomControllerV2 {
    private final ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap<>();

    private final RoomMapper roomMapper;

    public RoomControllerV2(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    @PostMapping("")
    public ResponseEntity<RoomDto> createRoom(
            @RequestAttribute("reqUserId") Long userId
    ) {
        Room room = new Room(userId);
        rooms.put(userId, room);
        System.out.println(rooms.get(userId));
        return ResponseEntity.ok(roomMapper.convert(userId, room));
    }




}
