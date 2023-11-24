package com.thomas.modules.chat.dto;

import java.util.List;

public class CreateGroupChatRequestDto {
    private List<String> usernames;
    private String name;
    public List<String> getUsernames() {
        return usernames;
    }
    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
