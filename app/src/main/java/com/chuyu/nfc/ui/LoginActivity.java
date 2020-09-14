package com.chuyu.nfc.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.ApplicationContext;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.Constants;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.UserBean;
import com.chuyu.nfc.cusview.ClearEditText;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.ParamUtil;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.tools.Tools;
import com.chuyu.nfc.tools.bar.SnackbarUtils;
import com.chuyu.nfc.tools.bar.StatusBarUtil;
import com.chuyu.nfc.tools.share.SharedPreferencesUtils;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    private boolean isRefresh;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.login_activity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        isRefresh = extras.getBoolean("isRefresh");
    }


    private ClearEditText phone;
    private EditText password;
    private TextView login;
    private TextView registered;
    private TextView forgetPassword;
    private ImageView back;
    private TextView version;

    @Override
    protected void initView() {
        StatusBarUtil.setImmersiveStatusBar(this, false);//深色
        phone = (ClearEditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        back = (ImageView) findViewById(R.id.back);
        registered = (TextView) findViewById(R.id.registered);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        version = (TextView) findViewById(R.id.version);
        login.setOnClickListener(this);
        back.setOnClickListener(this);
        registered.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        initEdit();
        phone.setKeyListener(DigitsKeyListener.getInstance());
        version.setText("Version "+Tools.getCurrentVersionName());
    }

    /**
     * @param paramAct tab标签
     */
    public static void startAct(Activity paramAct, boolean isRefresh) {
        Intent intent = new Intent(paramAct, LoginActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isRefresh", isRefresh);
        paramAct.startActivityForResult(intent, Constants.REQUEST_CODE);
    }


    @Override
    protected void initDatas() {
        //读取手机号和密码
        String mobileparam = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "mobile", "");
        String pwdparam = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "pwd", "");
        if (!mobileparam.isEmpty()) {
            phone.setText(mobileparam);
            phone.setSelection(mobileparam.length());//将光标移至文字末尾
        }
        if (!pwdparam.isEmpty()) {
            password.setText(pwdparam);

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String mobileparam = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "mobile", "");
        String pwdparam = (String) SharedPreferencesUtils.getParam(LoginActivity.this, "pwd", "");
        if (!mobileparam.isEmpty()) {
            phone.setText(mobileparam);
            phone.setSelection(mobileparam.length());//将光标移至文字末尾
        }
        if (!pwdparam.isEmpty()) {
            password.setText(pwdparam);
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == EventCode.CODE11) {
            if (eventCenter.getData().toString().equals("close")){
                finish();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                EventBus.getDefault().post(new EventCenter(EventCode.CODE30));
                break;
            case R.id.top_bar_leftImg:
                onBackPressed();
                break;
            case R.id.registered://注册
                startActivitySlide(RegisterActivity.class);
                break;
            case R.id.forget_password://忘记密码
                startActivitySlide(ForgotPasswordActivity.class);
                break;
            case R.id.login:// 登录
                String phones = phone.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                String forgetPasswords = forgetPassword.getText().toString().trim();
                if (phones.isEmpty() || phones.length() != 11) {
                    SnackbarUtils.Short(registered, "请输入正确手机号").show();
                    return;
                }
                if (TextUtils.isEmpty(passwords)) {
                    SnackbarUtils.Short(registered, "请输入登录密码!").show();
                    return;
                }
                if (TextUtils.isEmpty(forgetPasswords)) {
                    SnackbarUtils.Short(registered, "请输入登录密码!").show();
                    return;
                }
                login(phones, passwords);
                break;
            default:
                break;
        }
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
                            if (isRefresh) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isRefresh", isRefresh);
                                intent.putExtras(bundle);
                                setResult(Constants.RESPONSE_CODE_1001, intent);
                            }
                            ToastUtil.showTips(LoginActivity.this,"登录成功");
                            //手机号密码存起来
                            SharedPreferencesUtils.setParam(LoginActivity.this, "mobile", phone);
                            SharedPreferencesUtils.setParam(LoginActivity.this, "pwd", password);
                            EventBus.getDefault().post(new EventCenter(EventCode.CODE3, "首页"));
                            finish();
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

    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true

    private void initEdit() {
        final Drawable[] drawables = password.getCompoundDrawables();
        final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.drawable.open_eyes);
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // getWidth,getHeight必须在这里处理
                    float passwordMinX = v.getWidth() - eyeWidth - password.getPaddingRight();
                    float passwordMaxX = v.getWidth();
                    float passwordMinY = 0;
                    float passwordMaxY = v.getHeight();
                    float x = event.getX();
                    float y = event.getY();
                    if (x < passwordMaxX && x > passwordMinX && y > passwordMinY && y < passwordMaxY) {
                        // 点击了眼睛图标的位置
                        isHidePwd = !isHidePwd;
                        if (isHidePwd) {
                            password.setCompoundDrawables(drawables[0],
                                    drawables[1],
                                    drawables[2],
                                    drawables[3]);
                            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            password.setCompoundDrawables(drawables[0],
                                    drawables[1],
                                    drawableEyeOpen,
                                    drawables[3]);
                            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }
}
