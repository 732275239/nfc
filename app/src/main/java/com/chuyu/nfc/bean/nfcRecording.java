package com.chuyu.nfc.bean;

public class nfcRecording {
    private String name;
    private String urls;
    private String remark;

    public nfcRecording(String name, String urls, String remark) {
        this.name = name;
        this.urls = urls;
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "nfcRecording{" +
                "name='" + name + '\'' +
                ", urls='" + urls + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
