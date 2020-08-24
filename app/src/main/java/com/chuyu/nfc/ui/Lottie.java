package com.chuyu.nfc.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;

/**
 * Created by Zoello on 2019/5/28.
 */

public class Lottie extends BaseActivity {
//    private LottieAnimationView lottieview;
    private LottieAnimationView lottie1;
    private LottieAnimationView lottie2;
    private LottieAnimationView lottie3;

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    protected void initView() {
//        lottieview = (LottieAnimationView) findViewById(R.id.lottieview);
        lottie1 = (LottieAnimationView) findViewById(R.id.lottie1);
        lottie2 = (LottieAnimationView) findViewById(R.id.lottie2);
        lottie3 = (LottieAnimationView) findViewById(R.id.lottie3);
//        lottieview.useHardwareAcceleration(true);//硬件加速
//        lottie2.useHardwareAcceleration(true);
//        lottie3.useHardwareAcceleration(true);
        lottie1.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // 判断动画加载结束
                if (valueAnimator.getAnimatedFraction() == 1f) {
//                    if (dialog.isShowing() && getActivity() != null)
//                        dialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.lottieviews;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void onClick(View v) {

    }
}
