package com.chuyu.nfc.upgrade.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by xxu on 2018/5/23.
 */
public class AppUtil {



    /**
     * 获取当前客户端版本信息
     */
    public static int getCurrentVersion(Context ctx) {
        PackageInfo info = getPackageInfo(ctx);
        int curVersionCode = 0;
        if (info != null) {
            curVersionCode = info.versionCode;
        }
        return curVersionCode;
    }

    public static String getPackageName(Context ctx) {
        PackageInfo info = getPackageInfo(ctx);
        String packName = "";
        if (info != null){
            packName = info.packageName;
        }
        return packName;
    }

    public static String getQR_SOFT_KEY(Context ctx){
        String softKey="";
        try{
            ApplicationInfo appInfo = ctx.getPackageManager()
                    .getApplicationInfo(getPackageName(ctx),
                            PackageManager.GET_META_DATA);
            softKey=appInfo.metaData.getString("QRSOFT_APPID");
        }catch (Exception e){
            e.printStackTrace();
        }
        return softKey;

    }

    /**
     * 获取当前客户端版本信息
     */
    public static String getCurrentVersionName(Context ctx) {
        PackageInfo info = getPackageInfo(ctx);
        String curVersionName = "";
        if (info != null) {
            curVersionName = info.versionName;
        }
        return curVersionName;
    }

    public static PackageInfo getPackageInfo(Context ctx) {
        PackageInfo info = null;
        try {
            info = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(),
                            0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }




    public static String getApkSDPath(Context ctx) {
        try{
            File sdDir;
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
                File file = new File(sdDir.getPath() + "/"+getPackageName(ctx));
                if (!file.exists()) {
                    file.mkdirs();
                }
                return file.getPath() + "/";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
