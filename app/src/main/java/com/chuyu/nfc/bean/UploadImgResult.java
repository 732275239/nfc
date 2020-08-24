package com.chuyu.nfc.bean;

/**
 * Created by Administrator on 2018/12/6 0006.
 */

public class UploadImgResult implements java.io.Serializable{


    private UploadImgOriginal original;

    public UploadImgOriginal getOriginal() {
        return original;
    }

    public void setOriginal(UploadImgOriginal original) {
        this.original = original;
    }
}
