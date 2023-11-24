package com.thomas.modules.chat.dto;

import java.util.List;

public class RemoveUsersFromChatRequestDto {
    List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
