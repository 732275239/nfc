package com.chuyu.nfc.bean;

/**
 * Created by xxu on 2017/4/24.
 */
public class CaseUserInfo implements java.io.Serializable{

    private String uname;
    private String upwd;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }
}
