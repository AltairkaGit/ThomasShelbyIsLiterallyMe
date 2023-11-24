package com.thomas.modules.chat.dto;

import java.sql.Timestamp;

public class ReadChatMessagesDto {
    long chatId;
    Timestamp start;
    Timestamp end;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}
