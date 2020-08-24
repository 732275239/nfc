package com.chuyu.nfc.ui;

import android.os.Bundle;
import android.view.View;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;

import immortalz.me.library.TransitionsHeleper;

/**
 * Created by Zoello on 2019/5/24.
 */

public class JieLu extends BaseActivity {

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.jielu;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @Override
    protected void onDestroy() {
        TransitionsHeleper.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
