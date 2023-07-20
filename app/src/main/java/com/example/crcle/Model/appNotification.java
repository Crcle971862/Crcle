package com.example.crcle.Model;

public class appNotification {
    private String notificationBy,type,postID,postedBy,notificationID;
    private long notificationAt;
    private boolean checkOpen;

//    public appNotification() {
//    }
    public appNotification(String notificationBy, String type, String postID, String postedBy, String notificationID, long notificationAt, boolean checkOpen) {
        this.notificationBy = notificationBy;
        this.type = type;
        this.postID = postID;
        this.postedBy = postedBy;
        this.notificationID = notificationID;
        this.notificationAt = notificationAt;
        this.checkOpen = checkOpen;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }
}
