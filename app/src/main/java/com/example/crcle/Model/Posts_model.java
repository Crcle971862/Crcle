package com.example.crcle.Model;

public class Posts_model {
    String caption,postby;
    String  total_likes;
    String total_cmnts;
    String post_img;

    public Posts_model(String post_img, String caption, String total_likes, String total_cmnts, String postby) {
        this.post_img = post_img;
        this.caption = caption;
        this.total_likes = total_likes;
        this.total_cmnts = total_cmnts;
        this.postby=postby;
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

    //    public Posts_model(String post_name, String post_date, String total_like, String total_comments, int post_profile, int post_image, int post_like, int post_comment) {
//        this.post_name = post_name;
//        this.post_date = post_date;
//        this.total_like = total_like;
//        this.total_comments = total_comments;
//        this.post_profile = post_profile;
//        this.post_image = post_image;
//        this.post_like = post_like;
//        this.post_comment = post_comment;
//    }
//
//    public String getPost_name() {
//        return post_name;
//    }
//
//    public void setPost_name(String post_name) {
//        this.post_name = post_name;
//    }
//
//    public String getPost_date() {
//        return post_date;
//    }
//
//    public void setPost_date(String post_date) {
//        this.post_date = post_date;
//    }
//
//    public String getTotal_like() {
//        return total_like;
//    }
//
//    public void setTotal_like(String total_like) {
//        this.total_like = total_like;
//    }
//
//    public String getTotal_comments() {
//        return total_comments;
//    }
//
//    public void setTotal_comments(String total_comments) {
//        this.total_comments = total_comments;
//    }
//
//    public int getPost_profile() {
//        return post_profile;
//    }
//
//    public void setPost_profile(int post_profile) {
//        this.post_profile = post_profile;
//    }
//
//    public int getPost_image() {
//        return post_image;
//    }
//
//    public void setPost_image(int post_image) {
//        this.post_image = post_image;
//    }
//
//    public int getPost_like() {
//        return post_like;
//    }
//
//    public void setPost_like(int post_like) {
//        this.post_like = post_like;
//    }
//
//    public int getPost_comment() {
//        return post_comment;
//    }
//
//    public void setPost_comment(int post_comment) {
//        this.post_comment = post_comment;
//    }
}
