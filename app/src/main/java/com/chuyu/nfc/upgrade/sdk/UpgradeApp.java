package com.chuyu.nfc.upgrade.sdk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuyu.nfc.R;
import com.chuyu.nfc.tools.AlertUtils;
import com.chuyu.nfc.upgrade.NumberProgressBar;
import com.chuyu.nfc.upgrade.appconfig.ActConfig;
import com.chuyu.nfc.upgrade.bean.UpgradeInfo;
import com.chuyu.nfc.upgrade.net.BackResult;
import com.chuyu.nfc.upgrade.net.NetConnectTask;
import com.chuyu.nfc.upgrade.net.NetRequest;
import com.chuyu.nfc.upgrade.util.AppUtil;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by xxu on 2018/5/23.
 */
public class UpgradeApp {


    private Activity mActivity;
    private boolean isShowTip;


    public UpgradeApp(Activity mActivity, boolean isShowTip) {
        this.mActivity = mActivity;
        this.isShowTip = isShowTip;
    }

    /**
     * 注册千瑞软件APP升级
     *
     * @param application
     */
    public static void registUpload(Application application) {
        try {
            x.Ext.init(application);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查APP更新
     *
     * @param mActivity 当前Activity
     * @param isShowTip 是否显示加载提示,手动检查更新传true,APP启动更新传false
     */
    public static void checkUpgrade(Activity mActivity, boolean isShowTip) {
        if (mActivity == null) {
            return;
        }
        try {
            UpgradeApp upgradeApp = new UpgradeApp(mActivity, isShowTip);
            upgradeApp.initUpgradeData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initUpgradeData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("vcode", AppUtil.getCurrentVersion(mActivity));
        map.put("packageName", AppUtil.getPackageName(mActivity));
        map.put("qrkey", AppUtil.getQR_SOFT_KEY(mActivity));

        NetRequest.post(NetRequest.create(ActConfig.QRSOFT_APP_EVENT_UPGRADE, map), new NetConnectTask<UpgradeInfo>(new TypeToken<UpgradeInfo>() {
        }, mActivity) {

            @Override
            public void openLoading() {
                if (isShowTip) {
                    super.openLoading();
                }
            }

            @Override
            public void closeLoading() {
                if (isShowTip) {
                    super.closeLoading();
                }
            }

            @Override
            public void onSuccess(UpgradeInfo rsData, int eCode, String eMsg) {
                super.closeLoading();
                if (rsData == null) {
                    if (isShowTip) {
                        AlertUtils.alert(mActivity, "提示", "已是最新版本！");
                    }
                    return;
                }
                if (!TextUtils.isEmpty(rsData.getApk_url())) {
                    //弹出升级对话框
                    openUpgradeWin(rsData, false);
                } else {
                    if (isShowTip) {
                        AlertUtils.alert(mActivity, "提示", "已是最新版本！");
                    }
                }
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                if (isShowTip) {
                    super.onFailure(eCode, eMsg);
                } else {
                    super.closeLoading();
                }

            }
        });

    }


    public void openUpgradeWin(final UpgradeInfo upInfo, final boolean isShowTips) {
        String notis = "";
        if (upInfo.getApp_content() == null || "".equals(upInfo.getApp_content())) {
            notis = "有最新的版本需要更新";
        } else {
            notis = upInfo.getApp_content();
        }

        boolean isFocs = false;
        if (upInfo.getForce() != null && !upInfo.equals("")) {
            if (Integer.parseInt(upInfo.getForce()) >= 1) {
                isFocs = true;
            } else {
                isFocs = false;
            }
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity, R.style.AlertDialogCustom1);
        builder.setCancelable(false);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_upgrade, null);
        TextView biaoti = (TextView) view.findViewById(R.id.biaoti);
        TextView neirong = (TextView) view.findViewById(R.id.neirong);
        TextView queding = (TextView) view.findViewById(R.id.queding);
        TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
        biaoti.setText("升级提示");
        neirong.setText(notis);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(230), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(view);

        queding.setText("升级");
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startDownloadTask(dialog, upInfo.getApk_url(), upInfo.getId());
            }
        });
        if (!isFocs) {
            quxiao.setText("取消");
            quxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            quxiao.setVisibility(View.INVISIBLE);
        }

    }


    public void startDownloadTask(final DialogInterface dlog, final String downloadUrl, final String packageId) {
        String path = AppUtil.getApkSDPath(mActivity);
        if (TextUtils.isEmpty(path)) {
            AlertUtils.alert(mActivity, "错误提示", "检查更新失败，请检查存储卡！");
            return;
        }
        String apkName = "";
        try {
            int fileNameIndex = downloadUrl.lastIndexOf("/");
            if (fileNameIndex > 0) {
                apkName = downloadUrl.substring(fileNameIndex + 1, downloadUrl.length());
            }
            if (apkName.equals("")) {
                apkName = AppUtil.getPackageInfo(mActivity).packageName + ".apk";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        path = path + apkName;

        final String apkPath = path;

        try {

            List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
            permissionItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "读写存储卡权限", R.drawable.permission_ic_storage));

            HiPermission.create(mActivity)
                    .title("应用授权")
                    .permissions(permissionItems)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            Toast.makeText(mActivity, "升级失败，您已取消存储卡授权！", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish() {
                            try {
                                downloadFile(downloadUrl, apkPath, packageId);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mActivity, "升级失败，请稍后重试！", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            Toast.makeText(mActivity, "升级失败，您已拒绝存储卡权限！", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "升级失败，存储卡权限未打开！", Toast.LENGTH_LONG).show();
        }
    }



    public void downloadFile(final String url, final String path, final String packageId) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mActivity, R.style.AlertDialogCustom1);
        builder.setCancelable(false);
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_upgrade_progress, null);
        TextView biaoti = (TextView) view.findViewById(R.id.biaoti);
        final NumberProgressBar progress = (NumberProgressBar) view.findViewById(R.id.number_progress);
        biaoti.setText("新版本下载中!");
        progress.setProgress(0);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout(DensityUtil.dip2px(230), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setContentView(view);
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
                try {
                    float pressent = (float) current / total * 100;//i 增加数量，mBNumber 总数
                    progress.setProgress((int) pressent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(File result) {
                Toast.makeText(mActivity, "下载安装包成功！", Toast.LENGTH_SHORT).show();
                downloadApkEvent(packageId);//悄悄地通知服务器
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                installAPK(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AlertUtils.alert(mActivity, "错误提示", "下载安装包失败，请检查网络或存储卡!");

            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private void downloadApkEvent(String packageId) {
        Map<String, Object> map = new HashMap<String, Object>();
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
        try {
            if (file.exists()) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                //intent.setAction(Intent.ACTION_VIEW);
                //切记当要同时配Data和Type时一定要用这个方法，否则会出错
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //判断是否是AndroidN以及更高的版本
                if (Build.VERSION.SDK_INT >= 24) {
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mActivity, "com.zhaozheng.fileprovider", file);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                mActivity.startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                AlertUtils.alert(mActivity, "错误提示", "安装失败，安装包下载失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.alert(mActivity, "错误提示", "安装失败，安装包解析异常！");

        }

    }

    public String FormetFileSize(long file) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (file < 1024) {
            fileSizeString = df.format((double) file) + "B";
        } else if (file < 1048576) {
            fileSizeString = df.format((double) file / 1024) + "K";
        } else if (file < 1073741824) {
            fileSizeString = df.format((double) file / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) file / 1073741824) + "G";
        }
        return fileSizeString;
    }


}
