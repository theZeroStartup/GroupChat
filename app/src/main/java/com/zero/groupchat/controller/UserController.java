package com.zero.groupchat.controller;

import android.app.Application;

import com.zero.groupchat.model.User;

import java.util.List;

public class UserController extends Application {
    private String username;
    private String userId;
    private String imageUrl;
    private User user;
    private List<String> groupMembers;
    private List<User> userList;
    private static UserController instance;

    public UserController(String username, String userId, String imageUrl) {
        this.username = username;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public static UserController getInstance(){
        if (instance == null)
            instance = new UserController();
        return instance;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserController() {
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
