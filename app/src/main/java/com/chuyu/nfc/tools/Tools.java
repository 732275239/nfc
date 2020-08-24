package com.chuyu.nfc.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import com.chuyu.nfc.base.ApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 工具类
 */
public class Tools {
    /**
     * 文件保存路径
     */
    public static String FOLDER_PATH = "/QrSoft_Super/";

    public static void log(String logMessage) {
        LogUtil.v("demo", logMessage);
    }


    /**
     * 返回APP目录路径
     *
     * @return APP目录
     */
    public static String getAppFilePath() {
        return getSDPath();
    }

    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString() + "/";
        }
        return null;
    }

    public static String getDeviceImei(Context context){
        try{
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            return imei;
        }catch (Exception e){
            e.printStackTrace();
        }
        return System.currentTimeMillis()+"";
    }

    /**
     * 获得文件的路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            File file = new File(sdDir.getPath() + FOLDER_PATH);
            if (!file.exists())
                file.mkdirs();
            return file.getPath() + "/";
        }
        return null;
    }

    /**
     * 判断指定文件是否存在
     *
     * @param userQRcodeBitmapSavePath
     * @return
     */
    public static boolean hasFile(String userQRcodeBitmapSavePath) {
        File file = new File(userQRcodeBitmapSavePath);
        // 判断文件是否存在
        return file.exists();
    }

    /**
     * 获取当前客户端版本信息
     */
    public static int getCurrentVersion() {
        PackageInfo info = getPackageInfo();
        int curVersionCode = 0;
        if (info != null)
            curVersionCode = info.versionCode;
        return curVersionCode;
    }

    /**
     * 获取当前客户端版本信息
     */
    public static String getCurrentVersionName() {
        PackageInfo info = getPackageInfo();
        String curVersionName = "";
        if (info != null)
            curVersionName = info.versionName;
        return curVersionName;
    }

    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = ApplicationContext
                    .getInstance()
                    .getPackageManager()
                    .getPackageInfo(ApplicationContext.getInstance().getPackageName(),
                            0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info;
    }

    
    /**
	 * 
	 * @param is
	 * 
	 * @return
	 */
	public StringBuilder obtainStringFromInputStream(InputStream is) {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		try {
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb;
	}

	/**
	 * 
	 * 
	 * @param urlStr
	 * @return
	 */
	public InputStream getInputStreamFromUrl(String urlStr) {
		InputStream is = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000);
			is = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return is;
	}
	
	
	/**
	 * 关闭所有输入键盘
	 * @param activity
	 */
	public static  void finishEdit(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(activity.getWindow().getDecorView()
					.getWindowToken(), 0);
		}
	}



    public static Intent getBooleanIntent(String key,boolean value){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean(key, value);
        intent.putExtras(bundle);
        return intent;
    }

    public static String createJsBack(String... params){
        if(params==null){
            return "()";
        }
        if(params.length<=0){
            return "()";
        }
        String jsParams="";
        for(int i=0;i<params.length;i++){
            String p=params[i];
            if(p==null){
                p="";
            }
            if(i==0){
                jsParams="'"+p+"'";
            }else{
                jsParams=jsParams+","+"'"+p+"'";
            }
        }

        return "("+jsParams+")";
    }
}
