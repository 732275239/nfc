package com.chuyu.nfc.upgrade.sdk;

/**
 * Created by Administrator on 2018/10/24 0024.
 */

public interface UpgradeListen {

    public void hasNewVersion();

    public void checkError(String msg);

    public void installBegin();

    public void checkSuccessd();

}
