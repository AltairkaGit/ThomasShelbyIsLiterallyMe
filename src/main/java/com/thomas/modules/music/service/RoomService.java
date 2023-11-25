package com.thomas.modules.music.service;

import com.thomas.modules.music.model.Room;
import com.thomas.modules.music.model.RoomMessage;

import javax.naming.AuthenticationException;

public interface RoomService {
    Long extractRoomIdFromURI(String uri);
    boolean checkUserInRoom(Long userId, Long roomId);
    Room create(Long userId);
    Room getById(Long roomId) throws IllegalArgumentException;
    RoomMessage sendMessage(Long roomId, Long senderId, String content);
    void sendOffer(Long roomId, Long myId, Long UserId);
    void joinRoom(Long roomId, Long myId, String artifact) throws AuthenticationException;
    void acceptOffer(Long roomId, Long myId);
    void declineOffer(Long roomId, Long myId);
    void leave(Long roomId, Long userId);
    void removeUser(Long roomId, Long myId, Long userId);
    boolean isEmpty(Long roomId);
    void closeRoom(Long roomId);
}
