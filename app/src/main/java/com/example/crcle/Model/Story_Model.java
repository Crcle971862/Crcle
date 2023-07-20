package com.example.crcle.Model;

import java.util.ArrayList;
import java.util.List;

public class Story_Model {
    String storyby,profile_name;
    int profile_img;
    private ArrayList<User_stories> user_stories;

    public Story_Model(String profile_name, int profile_img) {
        this.profile_name = profile_name;
        this.profile_img = profile_img;
    }
    //    public Story_Model(int profile_img, String profile_name, ArrayList<User_stories> user_stories) {
//        this.profile_img = profile_img;
//        this.profile_name = profile_name;
//        this.user_stories = user_stories;
//    }

    public String getStoryby() {
        return storyby;
    }

    public void setStoryby(String storyby) {
        this.storyby = storyby;
    }

    public ArrayList<User_stories> getUser_stories() {
        return user_stories;
    }

    public void setUser_stories(ArrayList<User_stories> user_stories) {
        this.user_stories = user_stories;
    }

    public int getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(int profile_img) {
        this.profile_img = profile_img;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }
}
