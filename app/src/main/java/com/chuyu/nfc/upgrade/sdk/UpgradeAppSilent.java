package com.chuyu.nfc.upgrade.sdk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.chuyu.nfc.upgrade.appconfig.ActConfig;
import com.chuyu.nfc.upgrade.bean.UpgradeInfo;
import com.chuyu.nfc.upgrade.net.BackResult;
import com.chuyu.nfc.upgrade.net.NetConnectTask;
import com.chuyu.nfc.upgrade.net.NetRequest;
import com.chuyu.nfc.upgrade.util.AppUtil;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxu on 2018/5/23.
 */
public class UpgradeAppSilent {


    private Context mActivity;
    private UpgradeListen upgradeListen;


    public UpgradeAppSilent(Context mActivity, UpgradeListen upgradeListen){
        this.mActivity=mActivity;
        this.upgradeListen=upgradeListen;
    }

    /**
     * 注册千瑞软件APP升级
     * @param application
     */
    public static void registUpload(Application application){
        try{
            x.Ext.init(application);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 检查APP更新
     * @param mActivity 当前Activity
     */
    public static void checkUpgrade(Context mActivity, UpgradeListen upgradeListen){
        if(mActivity==null){
            return;
        }
        try{
            UpgradeAppSilent upgradeApp=new UpgradeAppSilent(mActivity,upgradeListen);
            upgradeApp.initUpgradeData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initUpgradeData(){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("vcode", AppUtil.getCurrentVersion(mActivity));
        map.put("packageName", AppUtil.getPackageName(mActivity));
        map.put("qrkey", AppUtil.getQR_SOFT_KEY(mActivity));

        NetRequest.post(NetRequest.create(ActConfig.QRSOFT_APP_EVENT_UPGRADE, map), new NetConnectTask<UpgradeInfo>(new TypeToken<UpgradeInfo>() {
        }, mActivity) {

            @Override
            public void openLoading() {

            }

            @Override
            public void closeLoading() {

            }

            @Override
            public void onSuccess(UpgradeInfo rsData, int eCode, String eMsg) {
                super.closeLoading();
                if (rsData == null) {
                    upgradeListen.hasNewVersion();
                    return;
                }
                if (!TextUtils.isEmpty(rsData.getApk_url())) {
                    startDownloadTask(rsData.getApk_url(),rsData.getId());
                } else {
                    upgradeListen.hasNewVersion();

                }
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                super.closeLoading();
                upgradeListen.checkError(eMsg);
            }
        });

    }





    public void startDownloadTask(final String downloadUrl, final String packageId){
        String path= AppUtil.getApkSDPath(mActivity);
        if(TextUtils.isEmpty(path)){
            upgradeListen.checkError("更新失败，存储卡暂不可用！");
            return;
        }
        String apkName="";
        try {
            int fileNameIndex = downloadUrl.lastIndexOf("/");
            if (fileNameIndex > 0) {
                apkName = downloadUrl.substring(fileNameIndex + 1, downloadUrl.length());
            }
            if (apkName.equals("")) {
                apkName = AppUtil.getPackageInfo(mActivity).packageName + ".apk";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        path=path+apkName;

        final String apkPath=path;

        try{

            downloadFile(downloadUrl, apkPath, packageId);
        }catch (Exception e){
            e.printStackTrace();
            upgradeListen.checkError("升级失败，存储卡权限未打开！");

        }
    }


    public void downloadFile(final String url, final String path, final String packageId) {

        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new org.xutils.common.Callback.ProgressCallback<File>() {

            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onSuccess(File result) {
                downloadApkEvent(packageId);//悄悄地通知服务器
                installAPK(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                upgradeListen.checkError("下载安装包失败，请检查网络或存储卡!");

            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }


    private void downloadApkEvent(String packageId){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("packageId", packageId);

        NetRequest.post(NetRequest.create(ActConfig.QRSOFT_APP_EVENT_APP_DOWN, map), new NetConnectTask<BackResult>(new TypeToken<BackResult>() {
        }, mActivity) {
            @Override
            public void openLoading() {

            }

            @Override
            public void closeLoading() {

            }

            @Override
            public void onSuccess(BackResult rsData, int eCode, String eMsg) {
                //super.onSuccess(rsData, eCode, eMsg);
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                //super.onFailure(eCode, eMsg);
            }
        });

    }


    /**
     * 开启安装APK页面的逻辑
     */
    private void installAPK(File file) {
        try{
            if(file.exists()) {

                upgradeListen.installBegin();

                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                String type = "application/vnd.android.package-archive";
                intent.setDataAndType(Uri.fromFile(file), type);
                mActivity.startActivity(intent);

                upgradeListen.checkSuccessd();
/*                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                //intent.setAction(Intent.ACTION_VIEW);
                //切记当要同时配Data和Type时一定要用这个方法，否则会出错
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                //判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= 24) {
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mActivity, AppUtil.getPackageName(mActivity) + ".fileProvider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mActivity.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());*/
            }else{
                upgradeListen.checkError("安装失败，安装包下载失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            upgradeListen.checkError("安装失败，安装包解析异常！");

        }

    }




}
