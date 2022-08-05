package com.zero.groupchat.model;

import android.net.Uri;

public class ChatList {

    private Uri userImage;
    private String username;
    private String messageCount;

    public ChatList() {
    }

    public ChatList(Uri userImage, String username) {
        this.userImage = userImage;
        this.username = username;
    }

    public ChatList(Uri userImage, String username, String messageCount) {
        this.userImage = userImage;
        this.username = username;
        this.messageCount = messageCount;
    }

    public Uri getUserImage() {
        return userImage;
    }

    public void setUserImage(Uri userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }
}
