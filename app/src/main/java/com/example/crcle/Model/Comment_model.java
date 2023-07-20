package com.example.crcle.Model;

public class Comment_model {
    String cmnt_body,cmnt_by;
    long postat;

    public Comment_model() {
    }

    public String getCmnt_body() {
        return cmnt_body;
    }

    public void setCmnt_body(String cmnt_body) {
        this.cmnt_body = cmnt_body;
    }

    public String getCmnt_by() {
        return cmnt_by;
    }

    public void setCmnt_by(String cmnt_by) {
        this.cmnt_by = cmnt_by;
    }

    public long getPostat() {
        return postat;
    }

    public void setPostat(long postat) {
        this.postat = postat;
    }


}
