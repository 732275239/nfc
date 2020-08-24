package com.chuyu.nfc.bean;

/**
 * Created by Administrator on 2018/12/6 0006.
 */

public class UploadImgOriginal implements java.io.Serializable{

    private String imgName;
    private String imgPath;
    private String imgCallPath;
    private String imgSrcUrl;
    private String type;

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgCallPath() {
        return imgCallPath;
    }

    public void setImgCallPath(String imgCallPath) {
        this.imgCallPath = imgCallPath;
    }

    public String getImgSrcUrl() {
        return imgSrcUrl;
    }

    public void setImgSrcUrl(String imgSrcUrl) {
        this.imgSrcUrl = imgSrcUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
