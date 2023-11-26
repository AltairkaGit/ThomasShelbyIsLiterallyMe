package com.thomas.modules.music.controller;

import com.thomas.modules.music.dto.RoomDto;
import com.thomas.modules.music.dto.mapper.RoomMapper;
import com.thomas.modules.music.model.Room;
import com.thomas.modules.music.model.RoomMessage;
import com.thomas.modules.music.service.RoomService;
import com.thomas.modules.security.websocket.RoomAuthorizationSubscription;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v2/room")
public class RoomControllerV2 {
    private final RoomMapper roomMapper;
    private final RoomService roomService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public RoomControllerV2(RoomMapper roomMapper, RoomService roomService, SimpMessagingTemplate simpMessagingTemplate) {
        this.roomMapper = roomMapper;
        this.roomService = roomService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @PostMapping("")
    public ResponseEntity<RoomDto> createRoom(
            @RequestAttribute("reqUserId") Long userId
    ) {
        Room room = roomService.create(userId);
        return ResponseEntity.ok(roomMapper.convert(userId, room));
    }

    @GetMapping("")
    public ResponseEntity<RoomDto> getRoom(
            @RequestAttribute("reqUserId") Long userId,
            @RequestParam("roomId") Long roomId
    ) {
        Room room = roomService.create(roomId);
        return ResponseEntity.ok(roomMapper.convert(userId, room));
    }

    @PutMapping("/{roomId}-{artifact}")
    public ResponseEntity<RoomDto> joinRoom(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable("roomId") Long roomId,
            @PathVariable("artifact") String artifact
    ) throws AuthenticationException {
        roomService.joinRoom(roomId, userId, artifact);
        Room room = roomService.getById(roomId);
        return ResponseEntity.ok(roomMapper.convert(userId, room));
    }

    @RoomAuthorizationSubscription
    @SubscribeMapping("/queue/room/{roomId}/chat")
    public void subscribeRoomChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomId
    ) {
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/chat")
    public void sendRoomChatMessage(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomIdParam,
            String content
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        Long roomId = Long.valueOf(roomIdParam);

        RoomMessage message = roomService.sendMessage(roomId, myId, content);
        System.out.println("" + roomId + " " + myId + " " + roomMapper.convertMessage(message).toString());
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/chat", roomMapper.convertMessage(message).toString());
    }

    @RoomAuthorizationSubscription
    @SubscribeMapping("/queue/room/{roomId}/voiceChat")
    public void subscribeChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomId
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/voiceChat")
    public void subscribeSignalingServer(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomId,
            String sessionDescriptionProtocol
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/voiceChat", sessionDescriptionProtocol);
    }

    @RoomAuthorizationSubscription
    @SubscribeMapping("/queue/room/{roomId}/users")
    public void subscribeNewRoomUsers(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomId
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
    }

    @SubscribeMapping("/app/queue/room/invites")
    public void subscribeRoomInvites(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") String roomId
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/users/offer/send")
    public void sendOffer(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") Long roomId,
            String userIdMessage
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        Long userId = Long.valueOf(userIdMessage);
        roomService.sendOffer(roomId, myId, userId);
        simpMessagingTemplate.convertAndSend("/app/queue/room/invites", roomId);
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/users/offer/accept")
    public void acceptOffer(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") Long roomId
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        roomService.acceptOffer(roomId, myId);
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/users", "accept:" + myId);
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/users/offer/decline")
    public void declineOffer(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") Long roomId
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        roomService.declineOffer(roomId, myId);
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/users", "decline:" + myId);
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/users/leave")
    public void leaveRoom(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") Long roomId
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        roomService.leave(roomId, myId);
        if (roomService.isEmpty(roomId)) roomService.closeRoom(roomId);
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/users", "left:" + myId);
    }

    @RoomAuthorizationSubscription
    @MessageMapping("/room/{roomId}/users/remove")
    public void removeUserFromRoom(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("roomId") Long roomId,
            String userIdMessage
    ) {
        Long myId = (Long)accessor.getSessionAttributes().get("userId");
        Long userId = Long.valueOf(userIdMessage);
        roomService.removeUser(roomId, myId, userId);
        if (roomService.isEmpty(roomId)) roomService.closeRoom(roomId);
        simpMessagingTemplate.convertAndSend("/app/queue/room/" + roomId + "/users", "left:" + userIdMessage);
    }

}
