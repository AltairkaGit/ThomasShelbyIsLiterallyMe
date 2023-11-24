package com.thomas.modules.chat.dto;

import java.util.List;

public class AddChatUsersRequestDto {
    private Integer chatId;
    private List<String> usernames;
    public Integer getChatId() {
        return chatId;
    }
    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
    public List<String> getUsernames() {
        return usernames;
    }
    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
