package com.chuyu.nfc.upgrade.net;


import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.chuyu.nfc.upgrade.util.UpUtils;
import com.google.gson.reflect.TypeToken;


/**
 * Created by xxu on 2018/5/23.
 */
public abstract class NetConnectTask<T> {

    protected TypeToken<T> rsType;
    private ProgressDialog loadingDialog;
    private Context appMContext;

    public NetConnectTask(TypeToken<T> typeToken, Context mContext) {
        this.appMContext = mContext;
        this.rsType = typeToken;
        openLoading();
    }

    private void startLoading(){
        if (appMContext != null) {
            try{
                if(loadingDialog!=null && loadingDialog.isShowing()){
                    return;
                }
                loadingDialog = new ProgressDialog(appMContext);
                loadingDialog.setMessage("数据加载中,请稍后...");
                loadingDialog.setCancelable(false);
                loadingDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void endLoading(){
        if (loadingDialog != null) {
            try{
                loadingDialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void openLoading() {
        startLoading();
    }

    /**
     * 关闭loading对话框
     */
    public void closeLoading() {
        endLoading();
    }


    public void onSuccess(T rsData, int eCode, String eMsg) {
        closeLoading();

    }


    public void onFailure(int eCode, String eMsg) {
        showErrorTips(eCode, eMsg);
    }

    public void showErrorTips(int eCode, String eMsg) {
        try {
            closeLoading();
            if (appMContext == null) {
                return;
            }
            if (loadingDialog != null && loadingDialog.isShowing()) {
                endLoading();
            }
            if (!TextUtils.isEmpty(eMsg)) {
                UpUtils.alert(appMContext, "错误", eMsg);
            } else {
                UpUtils.alert(appMContext, "错误", "错误码：" + eCode);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public TypeToken<T> doGetBackClass() {
        return this.rsType;
    }

}
