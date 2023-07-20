package com.example.crcle.Notification_model;

public class Fcm_Reqquest {
    String to;
    Data data;

    public Fcm_Reqquest(String to, Data data) {
        this.to = to;
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
