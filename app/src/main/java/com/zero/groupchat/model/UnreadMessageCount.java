package com.zero.groupchat.model;

public class UnreadMessageCount {

    private String userId;
    private int unreadMessageCount;

    public UnreadMessageCount() {
    }

    public UnreadMessageCount(String userId, int unreadMessageCount) {
        this.userId = userId;
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Override
    public String toString() {
        return "UnreadMessageCount{" +
                "userId='" + userId + '\'' +
                ", unreadMessageCount=" + unreadMessageCount +
                '}';
    }
}
