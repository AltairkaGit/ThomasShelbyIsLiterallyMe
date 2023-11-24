package com.thomas.modules.server.controller;

import com.thomas.lib.page.PageDto;
import com.thomas.modules.server.dto.*;
import com.thomas.modules.server.dto.mapper.ServerChannelMapper;
import com.thomas.modules.server.dto.mapper.ServerMapper;
import com.thomas.modules.server.dto.mapper.ServerUserMapper;
import com.thomas.modules.server.entity.ServerChannelEntity;
import com.thomas.modules.server.entity.ServerEntity;
import com.thomas.modules.server.entity.ServerUserEntity;
import com.thomas.modules.server.service.ConversationService;
import com.thomas.modules.server.service.ServerChannelService;
import com.thomas.modules.server.service.ServerUserService;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.server.service.ServerService;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v2/server")
public class ServerRestControllerV2 {
    private final UserService userService;
    private final ServerService serverService;
    private final ServerUserService serverUserService;
    private final ServerMapper serverMapper;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ServerChannelMapper serverChannelMapper;
    private final ServerChannelService serverChannelService;
    private final ServerUserMapper serverUserMapper;

    @Autowired
    public ServerRestControllerV2(
            UserService userService,
            ServerService serverService,
            ServerUserService serverUserService,
            ServerMapper serverMapper,
            ConversationService conversationService,
            SimpMessagingTemplate messagingTemplate,
            ServerChannelMapper serverChannelMapper,
            ServerChannelService serverChannelService,
            ServerUserMapper serverUserMapper) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverUserService = serverUserService;
        this.serverMapper = serverMapper;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
        this.serverChannelMapper = serverChannelMapper;
        this.serverChannelService = serverChannelService;
        this.serverUserMapper = serverUserMapper;
    }

    @GetMapping(value="")
    @Operation(summary = "get page of your servers")
    public ResponseEntity<PageDto<ServerDto>> getServers(
            @RequestAttribute("reqUserId") Long userId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<ServerEntity> servers = serverUserService.getUserServers(me, pageable);
        PageDto<ServerDto> res = PageDto.of(serverMapper.convertPage(servers));
        return ResponseEntity.ok(res);
    }

    @GetMapping(value="/{serverId}/users")
    @Operation(summary = "get page of your server users")
    public ResponseEntity<PageDto<ServerUserProfileDto>> getServerUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long serverId,
            Pageable pageable
    ) {
        UserEntity me = userService.getUserById(userId);
        ServerEntity server = serverService.getServerById(serverId).get();
        Page<ServerUserEntity> servers = serverUserService.getServerUsers(server, pageable);
        PageDto<ServerUserProfileDto> res = PageDto.of(serverUserMapper.convertPage(servers));
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="")
    @Operation(summary = "create a server")
    public ResponseEntity<ServerDto> createServer(
            @RequestAttribute("reqUserId") Long userId,
            CreateServerDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        ServerEntity server = serverService.createServer(me, dto.getServername());
        ServerDto res = serverMapper.convert(server);
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="/{serverId}/user")
    @Operation(summary = "add a user to server")
    public ResponseEntity<Void> addUsers(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long serverId,
            AddServerUserDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty()) throw new IllegalArgumentException("there is no server with id:" + serverId);
        if (!serverService.checkIfServerUser(me, serverId)) throw new SecurityException("user are not a server participant");
        UserEntity participant = userService.getUserById(dto.getUserid());
        serverUserService.addUser(serverId, participant.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/{serverId}")
    @Operation(summary = "get a server by id")
    public ResponseEntity<ServerExtendedDto> getServer(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long serverId
    ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");
        ServerExtendedDto res = serverMapper.convertExtended(server.get());
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="/{serverId}/channel")
    @Operation(summary = "create a channel")
    public ResponseEntity<ServerChannelDto> createChannel(
            @RequestAttribute("reqUserId") Long userId,
            @PathVariable Long serverId,
            @RequestBody CreateChannelDto dto
            ) {
        UserEntity me = userService.getUserById(userId);
        if (!serverService.checkIfServerUser(me, serverId))
            throw new SecurityException("you are not a participant of the server");
        Optional<ServerEntity> server = serverService.getServerById(serverId);
        if (server.isEmpty())
            throw new IllegalArgumentException("there is no server with this id");
        ServerChannelEntity channel = serverChannelService.createChannel(server.get(), dto.getChannelName(), dto.getChannelType());
        ServerChannelDto res = serverChannelMapper.convert(channel);
        return ResponseEntity.ok(res);
    }

    @SubscribeMapping("/queue/conversation/{conversationId}")
    public void subscribeChat(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " subscribed conversation " + conversation);
        //Set<String> room = conversationService.getRoomCopy(conversation);
        //conversationService.attachUser(conversation, myId);


        //todo: сосать член, сделать conversationEven json, потом сделать '/app/conversation/{chatId}/leave'
        //messagingTemplate.convertAndSend("/app/queue/conversation/" + conversation, sessionDescriptionProtocol);
    }

    @MessageMapping("/conversation/{conversationId}")
    public void sendMessage(
            SimpMessageHeaderAccessor accessor,
            @DestinationVariable("conversationId") String conversation,
            String sessionDescriptionProtocol
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));
        System.out.println("User: " + myId + " sends in conversation " + conversation + " message: " + sessionDescriptionProtocol);

        messagingTemplate.convertAndSend("/app/queue/conversation/" + conversation, sessionDescriptionProtocol);
    }
    /*
    @MessageMapping("/conversation/new")
    public void startConversation(
            SimpMessageHeaderAccessor accessor,
            String chatId
    ) {
        String myId = String.valueOf((Long)accessor.getSessionAttributes().get("userId"));

        //пизжу с бд id челов у которых есть этот канал

        for (userId : users)
            messagingTemplate.convertAndSendToUser(userId, "/app/queue/conversation/new", chatlId);
    }
    */
}
