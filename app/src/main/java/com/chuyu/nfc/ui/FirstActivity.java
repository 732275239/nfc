package com.chuyu.nfc.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.PermissionPageUtils;

import java.util.ArrayList;
import java.util.List;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

public class FirstActivity extends BaseActivity {

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_first;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initView() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //检查权限
                permisson();
            }
        }, 500);

    }

    private void closeThisPage() {
        Intent intent=new Intent(FirstActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    public void permisson(){
        List<PermissionItem> permissonItems = new ArrayList<PermissionItem>();
//        permissonItems.add(new PermissionItem(Manifest.permission.READ_PHONE_STATE, "您的手机型号", R.drawable.permission_ic_phone));
        permissonItems.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "储存图片", R.drawable.permission_ic_storage));
           permissonItems.add(new PermissionItem(Manifest.permission.CAMERA, "照相机", R.drawable.permission_ic_camera));
           permissonItems.add(new PermissionItem(Manifest.permission.NFC, "NFC", R.drawable.permission_ic_camera));
//        permissonItems.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION, "您的位置", R.drawable.permission_ic_location));

        HiPermission.create(this)
                .permissions(permissonItems)
                .filterColor(ResourcesCompat.getColor(getResources(), R.color.app_theme, getTheme()))
                .style(R.style.PermissionBlueStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {
                        //showToast("用户关闭权限申请");
                        AlertDialog.Builder builder = new AlertDialog.Builder(FirstActivity.this, R.style.AlertDialogCustom);
                        builder.setTitle("无法获取权限！");
                        builder.setMessage("请您手动选择-权限-开启相关权限");
                        builder.setCancelable(false);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PermissionPageUtils pageUtils =new PermissionPageUtils(FirstActivity.this);
                                pageUtils.jumpPermissionPage();
                                dialog.dismiss();
                                finish();
                            }
                        });


                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.create().show();
                    }

                    @Override
                    public void onFinish() {
                        //showToast("所有权限申请完成");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                closeThisPage();
                            }
                        }, 500);

                    }

                    @Override
                    public void onDeny(String permisson, int position) {

                    }

                    @Override
                    public void onGuarantee(String permisson, int position) {

                    }
                });
    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {

    }
}
