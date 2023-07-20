package com.example.crcle.Model;

public class Highlights_model {

    String detail,image;

    public Highlights_model(String image, String detail) {
        this.image = image;
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
