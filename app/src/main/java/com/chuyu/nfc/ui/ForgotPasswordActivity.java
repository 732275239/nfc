package com.chuyu.nfc.ui;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.ApplicationContext;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.UserBean;
import com.chuyu.nfc.cusview.ClearEditText;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.NetResult;
import com.chuyu.nfc.http.ParamUtil;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.share.SharedPreferencesUtils;
import com.chuyu.nfc.tools.bar.SnackbarUtils;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.tools.VerificationCodeTools;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Zoello on 2018/11/21.
 */

public class ForgotPasswordActivity extends BaseActivity implements VerificationCodeTools.Vercode_ResponseListener {
    private ImageView topBarLeftImg;
    private TextView topBarTitleTv;
    private ClearEditText phone;
    private EditText yzm;
    private TextView hqyzm;
    private EditText password;
    private EditText repeatpassword;
    private TextView registered;

    public static final int MSG_TIME_CODE = 0; // 倒计时
    public static final int MSG_RESEND_CODE = 1;// 重发

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.forgot_password_activity;
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
        topBarLeftImg = (ImageView) findViewById(R.id.top_bar_leftImg);
        topBarTitleTv = (TextView) findViewById(R.id.top_bar_titleTv);
        topBarTitleTv.setText("重置密码");
        phone = (ClearEditText) findViewById(R.id.phone);
        yzm = (EditText) findViewById(R.id.yzm);
        hqyzm = (TextView) findViewById(R.id.hqyzm);
        password = (EditText) findViewById(R.id.password);
        repeatpassword = (EditText) findViewById(R.id.repeatpassword);
        registered = (TextView) findViewById(R.id.registered);//注册
        hqyzm.setOnClickListener(this);
        topBarLeftImg.setOnClickListener(this);
        registered.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
//        if (eventCenter.getEventCode() == EventCode.CODE2) {
//            finish();
//        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hqyzm:
                String trim = phone.getText().toString().trim();
                //获取验证通用的方法
                if (!trim.isEmpty()) {
                    VerificationCodeTools.GetVerificationCOde(this, 2, trim, this);
                }
                break;
            case R.id.top_bar_leftImg:
                finish();
                break;
            case R.id.registered://更改密码
                String phone1 = phone.getText().toString().trim();
                String yzms = yzm.getText().toString().trim();
                String password1 = password.getText().toString().trim();
                String password2 = repeatpassword.getText().toString().trim();
                if (phone1.isEmpty()||phone1.length()!=11){
                    ToastUtil.showTips(this,"请输入正确手机号");
                    return;
                }
                if (yzms.isEmpty()){
                    ToastUtil.showTips(this,"请输入验证码");
                    return;
                }
                if (password1.isEmpty()){
                    ToastUtil.showTips(this,"请设置密码");
                    return;
                }else if (password1.length()<6){
                    ToastUtil.showTips(this,"密码至少6位数");
                    return;
                }
                 if (password2.isEmpty()){
                     ToastUtil.showTips(this,"请重复输入密码");
                    return;
                }else if (password2.length()<6){
                     ToastUtil.showTips(this,"密码至少6位数");
                    return;
                }
                if (!password1.equals(password2)){
                    ToastUtil.showTips(this,"两次输入的密码不一致");
                    return;
                }
                submit(phone1,yzms,password1);
                break;
        }
    }


    private void submit(final String mobile, final String yzm, final String pwd) {
        final Map<String, Object> map = ParamUtil.init();
        map.put("mobile", mobile);
        map.put("sms_code", yzm);
        map.put("pwd", pwd);

        CallNet.callNetNohttp(ParamUtil.create(URLs.MODIFYPWD, map), new ConnectTask<NetResult>(new TypeToken<NetResult>() {
        }, this) {
            @Override
            public void onSuccess(NetResult rsData, int eCode, String eMsg) {
                super.onSuccess(rsData, eCode, eMsg);
                    ToastUtil.showTips(ForgotPasswordActivity.this,"修改成功!");
//                    //手机号密码存起来
                    SharedPreferencesUtils.setParam(ForgotPasswordActivity.this, "pwd", pwd);
                    EventBus.getDefault().post(new EventCenter(EventCode.CODE11, "close"));
                    login(mobile,pwd);
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                super.onFailure(eCode, eMsg);
            }
        });
    }
    private void login(final String phone, final String password) {
        Map<String, Object> map = ParamUtil.init();
        map.put("mobile", phone);
        map.put("pwd", password);
        CallNet.callNetNohttp(ParamUtil.create(URLs.APP_LOGIN, map),
                new ConnectTask<UserBean>(new TypeToken<UserBean>() {
                }, this) {
                    @Override
                    public void onSuccess(UserBean rsData, int eCode, String eMsg) {
                        super.onSuccess(rsData, eCode, eMsg);
                        try {
                            ApplicationContext.getInstance().getSpTools().saveLogin(rsData);
                            EventBus.getDefault().post(new EventCenter(EventCode.CODE3, "首页"));
                            finish();
                            overridePendingTransition(R.anim.login_bg_bottom, R.anim.login_out_top);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int eCode, String eMsg) {
                        super.onFailure(eCode, eMsg);
                    }
                });
    }


    int i = 60;// 倒计时开始

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == MSG_TIME_CODE) {
            hqyzm.setText(i + "秒");
        } else if (msg.what == MSG_RESEND_CODE) {
            hqyzm.setText("重发");
            hqyzm.setClickable(true);
            i = 60;
        }
    }


    /**
     * 获取验证码后的回调
     *
     * @param key
     */

    public void ver_success(String key) {
        // 把按钮变成不可点击，并且显示倒计时（正在获取）
        hqyzm.setClickable(false);
        hqyzm.setText(i + "");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (; i > 0; i--) {
                    handler.sendEmptyMessage(MSG_TIME_CODE);// 倒计时
                    if (i <= 0) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(MSG_RESEND_CODE);// 重新发送
            }
        }).start();
    }


    public void ver_fail() {
        SnackbarUtils.Short(registered, "获取验证码失败!").show();
//        .backColor(0XFF116699)
    }

}

