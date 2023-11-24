package com.thomas.modules.security.websocket;

import com.thomas.modules.security.chatAuthorization.ChatAuthorizationService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChatAuthorizationSubscriptionAspect {
    private final ChatAuthorizationService chatAuthorizationService;

    @Autowired
    public ChatAuthorizationSubscriptionAspect(ChatAuthorizationService chatAuthorizationService) {
        this.chatAuthorizationService = chatAuthorizationService;
    }

    @Around("@annotation(ChatAuthorizationSubscription)")
    public Object authorizeSubscription(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] methodArgs = joinPoint.getArgs();
        SimpMessageHeaderAccessor accessor = (SimpMessageHeaderAccessor) methodArgs[0];
        Long userId = (Long)accessor.getSessionAttributes().get("userId");
        String dest = accessor.getDestination();
        Long chatId = chatAuthorizationService.extractChatIdFromURI(dest);

        if (!chatAuthorizationService.checkUserInChat(chatId, userId)) {
            return null; // Stop further processing
        }

        return joinPoint.proceed();
    }
}
