package com.chuyu.nfc.upgrade.bean;

/**
 * Created by xxu on 03/05/2018.
 */
public class UpgradeInfo implements java.io.Serializable{

    private String id;
    private String version_code;
    private String version_name;
    private String apk_url;
    private String app_content;
    private String force;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getApp_content() {
        return app_content;
    }

    public void setApp_content(String app_content) {
        this.app_content = app_content;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    @Override
    public String toString() {
        return "UpgradeInfo{" +
                "id='" + id + '\'' +
                ", version_code='" + version_code + '\'' +
                ", version_name='" + version_name + '\'' +
                ", apk_url='" + apk_url + '\'' +
                ", app_content='" + app_content + '\'' +
                ", force='" + force + '\'' +
                '}';
    }
}
