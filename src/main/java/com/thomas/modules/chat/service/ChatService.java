package com.thomas.modules.chat.service;

import com.thomas.modules.chat.entity.ChatEntity;
import com.thomas.modules.chat.exception.UserAlreadyInTheChatException;
import com.thomas.modules.file.entity.FileEntity;
import com.thomas.modules.user.entity.UserEntity;
import com.thomas.roles.ChatRole;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;

@Transactional
public interface ChatService extends ChatUserService {
    ChatEntity getById(Long chatId);
    ChatEntity createGroupChat(String name, UserEntity creator, Collection<UserEntity> users);
    ChatEntity craeteDirectChat(UserEntity creator, UserEntity partner) throws UserAlreadyInTheChatException;
    void delete(ChatEntity chat);
    void deleteById(Long chatId);
    void updatePicture(ChatEntity chat, FileEntity picture);
    void updateChatName(ChatEntity chat, String chatName);
    long countUnreadMessages(ChatEntity chat, UserEntity user, Timestamp origin);
    void addChatUserRole(ChatEntity chat, UserEntity user, ChatRole role);
}
