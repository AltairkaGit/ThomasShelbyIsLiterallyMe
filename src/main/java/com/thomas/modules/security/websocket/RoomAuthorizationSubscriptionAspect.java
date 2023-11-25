package com.thomas.modules.security.websocket;

import com.thomas.modules.music.service.RoomService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoomAuthorizationSubscriptionAspect {
    private final RoomService roomService;

    public RoomAuthorizationSubscriptionAspect(RoomService roomService) {
        this.roomService = roomService;
    }


    @Around("@annotation(RoomAuthorizationSubscription)")
    public Object authorizeSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        SimpMessageHeaderAccessor accessor = (SimpMessageHeaderAccessor) methodArgs[0];
        Long userId = (Long)accessor.getSessionAttributes().get("userId");
        String dest = accessor.getDestination();
        Long roomId = roomService.extractRoomIdFromURI(dest);

        if (!roomService.checkUserInRoom(userId, roomId)) {
            return null; // Stop further processing
        }

        return joinPoint.proceed();
    }
}
