package com.example.crcle.Model;

public class User_stories {
    String image;
    long story_timing;

    public User_stories(String image, long story_timing) {
        this.image = image;
        this.story_timing = story_timing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStory_timing() {
        return story_timing;
    }

    public void setStory_timing(long story_timing) {
        this.story_timing = story_timing;
    }
}
