package com.example.crcle.Model;

public class User {
    String name,mobile_no,password,user_image,fcm_token;
    String follower,userid,s;

    public User(String name, String mobile_no, String password,String fcm_token) {
        this.name = name;
        this.mobile_no = mobile_no;
        this.password = password;
        this.fcm_token=fcm_token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public User(String name, String user_image, String follower, String userid, String s) {
        this.name = name;
        this.user_image = user_image;
        this.follower = follower;
        this.userid = userid;
        this.s = s;
    }

    public User() {
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    Boolean follow;

    public User(String name, String user_image, String follower, String userid, Boolean follow) {
        this.name = name;
        this.user_image = user_image;
        this.follower = follower;
        this.userid = userid;
        this.follow = follow;
    }

    public Boolean getFollow() {
        return follow;
    }

    public void setFollow(Boolean follow) {
        this.follow = follow;
    }
}
