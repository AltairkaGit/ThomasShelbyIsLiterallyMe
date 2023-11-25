package com.thomas.modules.music.model;

import java.sql.Timestamp;
import java.time.Instant;

public class RoomMessage {
    private Long senderId;
    private String content;
    private Timestamp sendTimestamp;

    public RoomMessage(Long senderId, String content) {
        this.senderId = senderId;
        this.content = content;
        this.sendTimestamp = Timestamp.from(Instant.now());
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Timestamp sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }
}
