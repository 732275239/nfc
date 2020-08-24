package com.chuyu.nfc.bean;

public class company {

    private String uname;
    private String pos;

    public company(String uname, String pos) {
        this.uname = uname;
        this.pos = pos;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }
}
