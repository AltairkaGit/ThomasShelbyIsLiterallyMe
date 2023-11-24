package com.thomas.modules.chat.dto;

import java.util.List;

public class UpdateMessageDto {
    long messageId;
    String content;
    long replyId;
    List<String> fileUrls;

    public long getMessageId() {
        return messageId;
    }

    public String getContent() {
        return content;
    }

    public long getReplyId() {
        return replyId;
    }

    public List<String> getFileUrls() {
        return fileUrls;
    }
}
