package models;

import java.util.Map;

public class UpstreamMessage {

    private String from;
    private String category;
    private String messageId;
    private Map<String, String> dataPayload;

    public UpstreamMessage(String from, String category, String messageId, Map<String, String> dataPayload) {
        this.from = from;
        this.category = category;
        this.messageId = messageId;
        this.dataPayload = dataPayload;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Map<String, String> getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(Map<String, String> dataPayload) {
        this.dataPayload = dataPayload;
    }
}