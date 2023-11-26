package com.thomas.modules.chat.dto;

public class RoomMessageDto {
    private Long senderId;
    private String senderPictureUrl;
    private String content;
    private Long sendTimestamp;

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

    public String getSenderPictureUrl() {
        return senderPictureUrl;
    }

    public void setSenderPictureUrl(String senderPictureUrl) {
        this.senderPictureUrl = senderPictureUrl;
    }

    public Long getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Long sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    @Override
    public String toString() {
        return "RoomMessageDto{" +
                "senderId=" + senderId +
                ", senderPictureUrl='" + senderPictureUrl + '\'' +
                ", content='" + content + '\'' +
                ", sendTimestamp=" + sendTimestamp +
                '}';
    }
}
