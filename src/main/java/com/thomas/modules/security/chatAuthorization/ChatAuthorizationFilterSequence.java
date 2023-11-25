package com.thomas.modules.security.chatAuthorization;

import com.thomas.modules.security.composer.FilterSequence;
import com.thomas.modules.security.composer.context.ComposerContext;
import com.thomas.modules.security.composer.context.ComposerContextEnum;
import com.thomas.roles.ChatRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(2)
public class ChatAuthorizationFilterSequence implements FilterSequence {
    private final ChatAuthorizationService chatAuthorizationService;
    public ChatAuthorizationFilterSequence(ChatAuthorizationService chatAuthorizationService) {
        this.chatAuthorizationService = chatAuthorizationService;
    }

    /** Фильтр рассчитам на то что первичная авторизация пользователя прошла
     *  Тащим UserId из атрибутов запроса
     *  Тащим ChatId из URI
     *  Закидываем в контекст лист GrantedAuthorities чата
    **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, ComposerContext context)
            throws IOException, ServletException, AuthenticationException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Long userId = context.get(ComposerContextEnum.UserId);
        Long chatId = chatAuthorizationService.extractChatIdFromURI(httpServletRequest.getRequestURI());
        if (userId != null && chatId != null) {
            if (!chatAuthorizationService.checkUserInChat(chatId, userId))
                throw new AuthenticationException("user not in the chat");
        }
        List<ChatRole> roles = new ArrayList<>(List.of(ChatRole.Participant));
        List<ChatRole> userChatRoles = chatAuthorizationService.getUserChatRoles(chatId, userId);
        if (userChatRoles != null && !userChatRoles.isEmpty())
            roles.addAll(userChatRoles);
        context.put(ComposerContextEnum.ChatAuthorities, roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList()));
    }
}
