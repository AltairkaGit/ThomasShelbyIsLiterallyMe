package com.thomas.modules.chat.dto;

import com.thomas.modules.chat.entity.ChatEntity;
import io.swagger.v3.oas.annotations.media.Schema;

public class ChatResponseDto {
    private Long chatId;
    private String chatName;
    private String chatPictureUrl;
    @Schema
    private ChatEntity.ChatType chatType;
    private MessageResponseDto lastMessage;
    private long unreadMessages;
    private long chatUsersCount;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatPictureUrl() {
        return chatPictureUrl;
    }

    public void setChatPictureUrl(String chatPictureUrl) {
        this.chatPictureUrl = chatPictureUrl;
    }

    public ChatEntity.ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatEntity.ChatType chatType) {
        this.chatType = chatType;
    }

    public MessageResponseDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageResponseDto lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(long unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public long getChatUsersCount() {
        return chatUsersCount;
    }

    public void setChatUsersCount(long chatUsersCount) {
        this.chatUsersCount = chatUsersCount;
    }
}
