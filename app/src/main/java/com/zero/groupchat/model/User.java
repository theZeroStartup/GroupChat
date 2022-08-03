package com.zero.groupchat.model;

public class User {

    private String imgProfileUri;
    private String fullName;
    private String userName;
    private String userId;
    private int publisher;
    private boolean isAdded;

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
                "imgProfileUri=" + imgProfileUri +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", publisher=" + publisher +
                '}';
    }
}
