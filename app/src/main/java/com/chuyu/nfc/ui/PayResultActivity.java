package com.chuyu.nfc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.QrPayResult;
import com.chuyu.nfc.cusview.LoadingDailog;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.ParamUtil;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.ToastUtil;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

import cn.pedant.SweetAlert.OptAnimationLoader;
import cn.pedant.SweetAlert.SuccessTickView;

/**
 * Created by Zoello on 2018/11/14.
 * 支付结果
 */

public class PayResultActivity extends BaseActivity {


    private ImageView back;
    private TextView topBarTitleTv;
    private TextView name;
    private TextView phone;
    private TextView money;
    private TextView paytype;
    private LinearLayout orderinfo;
    private Button checkorder;
    private TextView payStatus;
    private TextView payText;
    private Button ok;

    private int type;
    private String orderno;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_payresult;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        type = extras.getInt("type");
        orderno = extras.getString("orderid");
    }

    /**
     * @param paramAct tab标签
     */
    public static void startAct(Activity paramAct, int type, String orderid) {
        Intent intent = new Intent(paramAct, PayResultActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("orderid", orderid);
        paramAct.startActivity(intent);
    }

    private FrameLayout mSuccessFrame;
    private View mSuccessRightMask;
    private View mSuccessLeftMask;
    private SuccessTickView mSuccessTick;
    private FrameLayout mErrorFrame;
    private ImageView mErrorX;
    private FrameLayout warningFrame;
    private ImageView warningX;
    private Animation mSuccessBowAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private TextView time;
    private TextView orderid;
    private LinearLayout phonelayout;

    @Override
    protected void initView() {

        mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
        mSuccessRightMask = (View) findViewById(R.id.mask_right);
        mSuccessLeftMask = (View) findViewById(R.id.mask_left);
        mSuccessTick = (SuccessTickView) findViewById(R.id.success_tick);
        mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
        mErrorX = (ImageView) findViewById(R.id.error_x);

        warningFrame = (FrameLayout) findViewById(R.id.warning_frame);
        warningX = (ImageView) findViewById(R.id.warning_x);

        back = (ImageView) findViewById(R.id.top_bar_leftImg);
        topBarTitleTv = (TextView) findViewById(R.id.top_bar_titleTv);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        money = (TextView) findViewById(R.id.money);
        paytype = (TextView) findViewById(R.id.paytype);
        orderinfo = (LinearLayout) findViewById(R.id.orderinfo);//支付信息布局
        payStatus = (TextView) findViewById(R.id.payStatus);//支付状态
        payText = (TextView) findViewById(R.id.payText);//支付提示
        time = (TextView) findViewById(R.id.time);//支付时间

        phonelayout = (LinearLayout) findViewById(R.id.phonelayout);

        orderid = (TextView) findViewById(R.id.orderid);//订单id
        checkorder = (Button) findViewById(R.id.checkorder);//查看订单按钮
        ok = (Button) findViewById(R.id.ok);//确定
        back.setOnClickListener(this);
        checkorder.setOnClickListener(this);
        ok.setOnClickListener(this);
        checkorder.setVisibility(View.GONE);//隐藏查看订单

        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(PayResultActivity.this, R.anim.success_mask_layout);
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(PayResultActivity.this, R.anim.success_bow_roate);
        mErrorInAnim = OptAnimationLoader.loadAnimation(PayResultActivity.this, R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(PayResultActivity.this, R.anim.error_x_in);

        final LoadingDailog loading = getLoading();
        loading.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        setAnimation();//动画
                    }
                });

            }
        }, 1000);
    }


    public void setAnimation() {
        if (type == 1) {
            mSuccessFrame.setVisibility(View.VISIBLE);
            mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
            mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
            topBarTitleTv.setText("支付成功");
            payStatus.setText("支付成功");
            payText.setText("高峰期支付会有延迟");
            orderinfo.setVisibility(View.VISIBLE);

        } else if (type == 2) {
            mErrorFrame.setVisibility(View.VISIBLE);
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);

            topBarTitleTv.setText("支付失败");
            payStatus.setText("支付失败");
            payText.setText("订单支付失败,需要重新支付");
            orderinfo.setVisibility(View.GONE);
            checkorder.setVisibility(View.GONE);
            ok.setText("重新支付");
        } else if (type == 3) {
            warningFrame.setVisibility(View.VISIBLE);
            warningFrame.startAnimation(mErrorInAnim);
            warningX.startAnimation(mErrorXInAnim);

            topBarTitleTv.setText("支付超时");
            payStatus.setText("支付超时");
            payText.setText("订单支付超时,需要重新支付");
            orderinfo.setVisibility(View.GONE);
            checkorder.setVisibility(View.GONE);
            ok.setText("返回");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_leftImg:
                finish();
                break;
            case R.id.checkorder://查看订单
                ToastUtil.showTips(this, "正在测试");
                break;
            case R.id.ok://确定or 继续浏览
                Intent intent = new Intent(PayResultActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();
                break;


        }
    }


    @Override
    protected void initDatas() {
        Map<String, Object> map = ParamUtil.init();
        map.put("order_no", orderno);
        CallNet.callNetNohttp(ParamUtil.createMy(URLs.PAYORDERDETAILS, map), new ConnectTask<QrPayResult>(new TypeToken<QrPayResult>() {
        }, this) {

            @Override
            public void openLoading() {
            }

            @Override
            public void onSuccess(QrPayResult rsData, int eCode, String eMsg) {
                super.closeLoading();
                if (rsData != null) {
                    orderinfo.setVisibility(View.VISIBLE);
                    String type = rsData.getType();
                    if (type.equals("mechanism")) { //机构订单
                        orderid.setText(rsData.getOrder_no());
                        name.setText(rsData.getOccupant_names());
                        phonelayout.setVisibility(View.GONE);
                        money.setText("¥" + rsData.getPay_amount());
                        paytype.setText(rsData.getPay_method());
                        time.setText(rsData.getPay_time());
                    } else if (type.equals("agent")) {// 代买代办订单
                        orderid.setText(rsData.getOrder_no());
                        name.setText(rsData.getUser_name());
                        phone.setText(rsData.getUser_phone());
                        money.setText("¥" + rsData.getPay_amount());
                        paytype.setText(rsData.getPay_method());
                        time.setText(rsData.getPay_time());
                    } else if (type.equals("housewifery")) {// 家政订单
                        orderid.setText(rsData.getOrder_no());
                        name.setText(rsData.getUser_name());
                        phone.setText(rsData.getUser_phone());
                        money.setText("¥" + rsData.getPay_amount());
                        paytype.setText(rsData.getPay_method());
                        time.setText(rsData.getPay_time());
                    } else if (type.equals("nursing")) {//  护理订单
                        orderid.setText(rsData.getOrder_no());
                        name.setText(rsData.getUser_name());
                        phone.setText(rsData.getUser_phone());
                        money.setText("¥" + rsData.getPay_amount());
                        paytype.setText(rsData.getPay_method());
                        time.setText(rsData.getPay_time());
                    } else if (type.equals("product")) {//  商品订单
                        orderid.setText(rsData.getOrder_no());
                        name.setText(rsData.getReceive_name());
                        phone.setText(rsData.getReceive_tel());
                        money.setText("¥" + rsData.getOrder_amount());
                        paytype.setText(rsData.getPayment_method());
                        time.setText(rsData.getPayment_time());
                    }
                }

            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                closeLoading();
                orderinfo.setVisibility(View.GONE);
                ToastUtil.showTips(PayResultActivity.this, "网络出问题了,请到订单详情查看");
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


}

