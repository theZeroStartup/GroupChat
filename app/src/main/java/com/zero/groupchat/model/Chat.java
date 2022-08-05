package com.zero.groupchat.model;

public class Chat {

    private String sender;
    private String senderId;
    private String senderImg;
    private String message;
    private String timestamp;

    public Chat() {
    }

    public Chat(String sender, String senderId, String senderImg, String message, String timestamp) {
        this.sender = sender;
        this.senderId = senderId;
        this.senderImg = senderImg;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderImg() {
        return senderImg;
    }

    public void setSenderImg(String senderImg) {
        this.senderImg = senderImg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "Chat{" +
                "sender='" + sender + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderImg='" + senderImg + '\'' +
                ", message='" + message + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
