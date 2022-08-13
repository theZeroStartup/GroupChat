package com.zero.groupchat.model;

public class User {

    private String imgProfileUri;
    private String fullName;
    private String userName;
    private String userId;
    private String chatId;
    private int publisher;
    private boolean isAdded;
    private UnreadMessageCount messageUnreadCount;

    public User() {
    }

    public User(String userImage, String username) {
        this.imgProfileUri = userImage;
        this.fullName = username;
    }

    public User(String imgProfileUri, String fullName, String userName, int publisher) {
        this.imgProfileUri = imgProfileUri;
        this.fullName = fullName;
        this.userName = userName;
        this.publisher = publisher;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public String getImgProfileUri() {
        return imgProfileUri;
    }

    public void setImgProfileUri(String imgProfileUri) {
        this.imgProfileUri = imgProfileUri;
    }

    public UnreadMessageCount getMessageUnreadCount() {

        return messageUnreadCount;
    }

    public void setMessageUnreadCount(UnreadMessageCount messageUnreadCount) {
        this.messageUnreadCount = messageUnreadCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPublisher() {
        return publisher;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }


    @Override
    public String toString() {
        return "User{" +
                "imgProfileUri='" + imgProfileUri + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", chatId='" + chatId + '\'' +
                ", publisher=" + publisher +
                ", isAdded=" + isAdded +
                ", messageUnreadCount=" + messageUnreadCount +
                '}';
    }
}
