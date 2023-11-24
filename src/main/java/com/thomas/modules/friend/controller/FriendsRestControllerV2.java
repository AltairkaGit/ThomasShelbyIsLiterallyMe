package com.thomas.modules.friend.controller;

import com.thomas.lib.page.PageDto;
import com.thomas.modules.friend.dto.DeleteFriendDto;
import com.thomas.modules.friend.dto.AcceptOfferDto;
import com.thomas.modules.friend.dto.RejectOfferDto;
import com.thomas.modules.friend.dto.SendOfferDto;
import com.thomas.modules.user.dto.UserProfileResponseDto;
import com.thomas.modules.user.dto.mapper.UserProfileResponseMapper;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.modules.friend.service.FriendsService;
import com.thomas.modules.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v2")
public class FriendsRestControllerV2 {
    private final FriendsService friendsService;
    private final UserService userService;
    private final UserProfileResponseMapper userProfileResponseMapper;

    @Autowired
    public FriendsRestControllerV2(FriendsService friendsService, UserService userService, UserProfileResponseMapper userProfileResponseMapper) {
        this.friendsService = friendsService;
        this.userService = userService;
        this.userProfileResponseMapper = userProfileResponseMapper;
    }

    @GetMapping(value="/friends")
    @Operation(summary = "get a page of your friends")
    public ResponseEntity<PageDto<UserProfileResponseDto>> getFriends(
            @RequestAttribute("reqUserId") Long userId,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<UserEntity> paged = friendsService.getFriends(me, Pageable.ofSize(size).withPage(page));
        PageDto<UserProfileResponseDto> res = PageDto.of(userProfileResponseMapper.convertPage(paged));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping(value="/friends")
    @Operation(summary = "delete a friend, 200 if ok, 404 otherwise")
    public ResponseEntity<Void> deleteFriend(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody DeleteFriendDto dto
            ) {
        UserEntity me = userService.getUserById(userId);
        UserEntity friend = userService.getUserById(dto.getFriendId());
        friendsService.deleteFriend(me, friend);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/offers")
    @Operation(summary = "get a page of incoming friend offers")
    public ResponseEntity<PageDto<UserProfileResponseDto>> getOffers(
            @RequestAttribute("reqUserId") Long userId,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        UserEntity me = userService.getUserById(userId);
        Page<UserEntity> paged = friendsService.getOffers(me, Pageable.ofSize(size).withPage(page));
        PageDto<UserProfileResponseDto> res = PageDto.of(userProfileResponseMapper.convertPage(paged));
        return ResponseEntity.ok(res);
    }

    @PostMapping(value="/offer")
    @Operation(summary = "send friends offer to user, 200 if ok, 404 otherwise")
    public ResponseEntity<Void> sendOffer(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody SendOfferDto dto
            ) {
        UserEntity me = userService.getUserById(userId);
        UserEntity user = userService.getUserById(dto.getUserId());
        friendsService.sendOffer(me, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value="/offer")
    @Operation(summary = "accept offer, you send me id of user you want to accept, 200 if ok, 404 otherwise")
    public ResponseEntity<Void> acceptOffer(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody AcceptOfferDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        UserEntity user = userService.getUserById(dto.getUserId());
        friendsService.acceptOffer(friendsService.getOffer(user, me));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value="/offer")
    @Operation(summary = "reject offer, you send me id of user you want to reject, 200 if ok, 404 otherwise")
    public ResponseEntity<Void> rejectOffer(
            @RequestAttribute("reqUserId") Long userId,
            @RequestBody RejectOfferDto dto
    ) {
        UserEntity me = userService.getUserById(userId);
        UserEntity user = userService.getUserById(dto.getUserId());
        friendsService.rejectOffer(friendsService.getOffer(user, me));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
