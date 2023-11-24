package com.thomas.modules.security.chatAuthorization;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.roles.ChatRole;

import java.util.List;

public interface ChatAuthorizationService {
    Long extractChatIdFromURI(String uri);
    boolean checkUserInChat(ChatEntity chat, UserEntity user);
    boolean checkUserInChat(Long chatId, Long userId);
    List<ChatRole> getUserChatRoles(ChatEntity chat, UserEntity user);
    List<ChatRole> getUserChatRoles(Long chatId, Long userId);
}
