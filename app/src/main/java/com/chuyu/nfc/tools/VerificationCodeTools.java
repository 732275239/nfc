package com.chuyu.nfc.tools;


import com.google.gson.reflect.TypeToken;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.SmsKey;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.NetResult;
import com.chuyu.nfc.http.ParamUtil;

import java.util.Map;

/**
 * 名称：获取验证码的工具类
 */

public class VerificationCodeTools {
    private static Vercode_ResponseListener mListener;

    /**
     * 获取验证通用的方法
     *
     * @param activity
     * @param type     type:1注册
     * @param phone    手机号码
     * @param listener 发送验证码的回调
     */
    public static void GetVerificationCOde(final BaseActivity activity, int type, final String phone, Vercode_ResponseListener listener) {
        mListener = listener;
        final Map<String, Object> map = ParamUtil.init();
        map.put("mobile", phone);
        map.put("type", type);
        CallNet.callNetNohttp(ParamUtil.create(URLs.getVcode, map), new ConnectTask<SmsKey>(new TypeToken<SmsKey>() {
        }, activity) {
            @Override
            public void onSuccess(SmsKey rsData, int eCode, String eMsg) {
                super.onSuccess(rsData, eCode, eMsg);
                if (rsData != null) {
                    //成功后接着调2.3接口  获取验证码
                    getYzm(activity, phone, rsData.getSms_key());
                } else {
                    ToastUtil.showTips(activity, "获取验证码失败!");
                }
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                super.onFailure(eCode, eMsg);
                if (mListener != null) {
                    mListener.ver_fail();
                }
            }
        });

    }

    private static void getYzm(final BaseActivity activity, String phoneNum, final String vscode) {
        final Map<String, Object> map = ParamUtil.init();
        map.put("sms_key", vscode);
        CallNet.callNetNohttp(ParamUtil.create(URLs.getSMSCode, map), new ConnectTask<NetResult>(new TypeToken<NetResult>() {
        }, activity) {

            @Override
            public void openLoading() {
            }

            @Override
            public void onSuccess(NetResult rsData, int eCode, String eMsg) {
                super.onSuccess(rsData, eCode, eMsg);

                ToastUtil.showTips(activity, "验证码已发送!");
                if (mListener != null) {
                    mListener.ver_success(vscode);
                }
            }


            @Override
            public void onFailure(int eCode, String eMsg) {
                super.onFailure(eCode, eMsg);
                if (mListener != null) {
                    mListener.ver_fail();
                }
            }
        });
    }


    public interface Vercode_ResponseListener {
        /**
         * 获取验证成功后的回调
         */
        void ver_success(String key);

        /**
         * 获取验证码失败后的回调
         */
        void ver_fail();
    }
}
