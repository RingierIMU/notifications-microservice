package models;


import java.util.Map;

public class DownstreamMessage {

    private String to;
    private String condition;
    private String messageId;
    private String collapseKey;
    private String priority;
    private Boolean contentAvailable;
    private Boolean delayWhileIdle;
    private Integer timeToLive;
    private Boolean deliveryReceiptRequested;
    private Boolean dryRun;
    private Map<String, String> dataPayload;
    private Map<String, String> notificationPayload;

    public DownstreamMessage(String to, String messageId, Map<String, String> dataPayload) {
        this.to = to;
        this.messageId = messageId;
        this.dataPayload = dataPayload;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getCollapseKey() {
        return collapseKey;
    }

    public void setCollapseKey(String collapseKey) {
        this.collapseKey = collapseKey;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean isContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(Boolean contentAvailable) {
        this.contentAvailable = contentAvailable;
    }

    public Boolean isDelayWhileIdle() {
        return delayWhileIdle;
    }

    public void setDelayWhileIdle(Boolean delayWhileIdle) {
        this.delayWhileIdle = delayWhileIdle;
    }

    public Integer getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(Integer timeToLive) {
        this.timeToLive = timeToLive;
    }

    public Boolean isDeliveryReceiptRequested() {
        return deliveryReceiptRequested;
    }

    public void setDeliveryReceiptRequested(Boolean deliveryReceiptRequested) {
        this.deliveryReceiptRequested = deliveryReceiptRequested;
    }

    public Boolean isDryRun() {
        return dryRun;
    }

    public void setDryRun(Boolean dryRun) {
        this.dryRun = dryRun;
    }

    public Map<String, String> getDataPayload() {
        return dataPayload;
    }

    public void setDataPayload(Map<String, String> dataPayload) {
        this.dataPayload = dataPayload;
    }

    public Map<String, String> getNotificationPayload() {
        return notificationPayload;
    }

    public void setNotificationPayload(Map<String, String> notificationPayload) {
        this.notificationPayload = notificationPayload;
    }

}