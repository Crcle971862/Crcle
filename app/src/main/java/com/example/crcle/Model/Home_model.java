package com.example.crcle.Model;

import java.util.List;

public class Home_model {
    public static final int user_story=0;
    public static final int posts=1;
    public int getviewtype;

    //////////////////////////story///////////
    private List<Story_Model> story_modelList;

    public Home_model(int getviewtype, List<Story_Model> story_modelList) {
        this.getviewtype = getviewtype;
        this.story_modelList = story_modelList;
    }
    public int getGetviewtype() {
        return getviewtype;
    }

    public void setGetviewtype(int getviewtype) {
        this.getviewtype = getviewtype;
    }

    public List<Story_Model> getStory_modelList() {
        return story_modelList;
    }

    public void setStory_modelList(List<Story_Model> story_modelList) {
        this.story_modelList = story_modelList;
    }
//////////////////////////story///////////
    String caption,postby,total_likes,total_cmnts,post_img,postid,date;

    public Home_model(int getviewtype,String post_img, String caption, String total_likes, String total_cmnts, String postby,String postid,String date) {
        this.post_img = post_img;
        this.caption = caption;
        this.total_likes = total_likes;
        this.total_cmnts = total_cmnts;
        this.postby=postby;
        this.getviewtype=getviewtype;
        this.postid=postid;
        this.date=date;
    }

    public String getPostby() {
        return postby;
    }

    public void setPostby(String postby) {
        this.postby = postby;
    }

    public String getPost_img() {
        return post_img;
    }

    public void setPost_img(String post_img) {
        this.post_img = post_img;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(String total_likes) {
        this.total_likes = total_likes;
    }

    public String getTotal_cmnts() {
        return total_cmnts;
    }

    public void setTotal_cmnts(String total_cmnts) {
        this.total_cmnts = total_cmnts;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
