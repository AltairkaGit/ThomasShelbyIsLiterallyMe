package com.thomas.modules.chat.dto;

import java.sql.Timestamp;
import java.util.List;

public class MessageWebsocketDto {
    private Timestamp sendTimestamp;
    private String content;
    private Long replyId;
    private List<Long> fileIds;

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

    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public List<Long> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }
}
