package com.chuyu.nfc.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chuyu.nfc.R;
import com.chuyu.nfc.adapter.TestAdapter;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.cusview.recyclerview.SimpleItemDecoration;
import com.chuyu.nfc.tools.EventBus.EventCenter;

import java.util.ArrayList;

/**
 * Created by Zoello on 2019/5/28.
 */

public class XuanFu extends BaseActivity {

    private RecyclerView recyclerView;

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

    }

    @Override
    protected void initDatas() {
        final ArrayList<String> provinces = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            provinces.add("111");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new LinearLayoutManager(XuanFu.this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new SimpleItemDecoration(XuanFu.this, 0));
                TestAdapter adapter = new TestAdapter(XuanFu.this, provinces);
                recyclerView.setAdapter(adapter);
            }
        },2000);

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.xuanfu_layout;
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
