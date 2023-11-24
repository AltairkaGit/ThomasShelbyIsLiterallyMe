package com.thomas.modules.chat.dto;

import java.sql.Timestamp;
import java.util.List;

public class MessageResponseDto {
    private Long messageId;
    private Timestamp sendTimestamp;
    private String content;
    private Long chatId;
    private Long senderId;
    private Long replyId;
    private String replyContent;
    private List<String> fileUrls;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Timestamp getSendTimestamp() {
        return sendTimestamp;
    }

    public void setSendTimestamp(Timestamp sendTimestamp) {
        this.sendTimestamp = sendTimestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls) {
        this.fileUrls = fileUrls;
    }
}
