package com.zero.groupchat.model;

import android.net.Uri;

public class MyChat {

    private String groupImage;
    private String chatId;
    private String groupName;

    public MyChat() {
    }

    public MyChat(String groupImage, String chatId, String groupName) {
        this.groupImage = groupImage;
        this.chatId = chatId;
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
