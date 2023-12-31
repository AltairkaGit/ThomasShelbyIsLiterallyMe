package com.thomas.modules.music.service.impl;

import com.thomas.modules.music.model.Room;
import com.thomas.modules.music.model.RoomMessage;
import com.thomas.modules.music.service.RoomService;
import com.thomas.modules.music.service.TrackService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.naming.AuthenticationException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RoomServiceImpl implements RoomService {
    private final ConcurrentHashMap<Long, Room> rooms = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;

    public RoomServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Room create(Long userId) {
        Room room = new Room(userId, messagingTemplate);
        rooms.put(userId, room);
        System.out.println(rooms.get(userId));
        return room;
    }

    @Override
    public StreamingResponseBody getStream(Long roomId) {
        Room room = rooms.get(roomId);
        return room.getStream();
    }

    @Override
    public Long getPlayingNow(Long roomId) {
        Room room = rooms.get(roomId);
        return room.getPlayingNow();
    }

    @Override
    public RoomMessage sendMessage(Long roomId, Long senderId, String content) {
        Room room = rooms.get(roomId);
        return room.sendMessage(senderId, content);
    }

    @Override
    public void sendOffer(Long roomId, Long myId, Long userId) {
        Room room = rooms.get(roomId);
        room.sendOffer(myId, userId);
    }

    @Override
    public void acceptOffer(Long roomId, Long myId) {
        Room room = rooms.get(roomId);
        room.acceptOffer(myId);
    }

    @Override
    public void declineOffer(Long roomId, Long myId) {
        Room room = rooms.get(roomId);
        room.declineOffer(myId);
    }

    @Override
    public void leave(Long roomId, Long userId) {
        Room room = rooms.get(roomId);
        room.leave(userId);
    }

    @Override
    public void joinRoom(Long roomId, Long myId, String artifact) throws AuthenticationException {
        Room room = rooms.get(roomId);
        System.out.println("Hello link");
        System.out.println(room.getOwnerId());
        System.out.println(roomId);
        if (!Objects.equals(room.getOwnerId(), roomId))
            throw new AuthenticationException("link is invalid");
        room.joinRoom(myId);
    }

    @Override
    public Room getById(Long roomId) throws IllegalArgumentException {
        Room room = rooms.get(roomId);
        if (room == null) throw new IllegalArgumentException("the room is no longer exist");
        return room;
    }

    @Override
    public void removeUser(Long roomId, Long myId, Long userId) {
        Room room = rooms.get(roomId);
        room.removeUser(myId, userId);
    }

    @Override
    public boolean isEmpty(Long roomId) {
        Room room = rooms.get(roomId);
        return room.getUsers().isEmpty();
    }

    @Override
    public void closeRoom(Long roomId) {
        rooms.remove(roomId);
    }

    @Override
    public boolean checkUserInRoom(Long userId, Long roomId) {
        Room room = rooms.get(roomId);
        return room.getUsers().contains(userId);
    }

    @Override
    public Long extractRoomIdFromURI(String uri) {
        Matcher matcher = chatIdPattern.matcher(uri);
        if (matcher.find()) {
            String idStr = matcher.group(1);
            return Long.parseLong(idStr);
        }
        return null;
    }

    private static final Pattern chatIdPattern = Pattern.compile("/room/(\\d+)");
}
