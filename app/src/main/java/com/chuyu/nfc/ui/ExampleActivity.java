package com.chuyu.nfc.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;

import java.util.ArrayList;

/**
 * Created by Zoello on 2019/5/23.
 * 各种例子
 */

public class ExampleActivity extends BaseActivity {

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private ImageView img4;
    private ImageView img5;
    private ImageView img6;
    private ImageView huaru;
    private FloatingActionButton bt;
    private Button lottie;
    private Button xuanfu;

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.exampleactivity;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1:
            case R.id.img2:
            case R.id.img3://共享
                startActivity(new Intent(this, ShareActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this,
                                android.util.Pair.create((View)img1, "shareimg1"),
                                android.util.Pair.create((View)img2, "shareimg2"),
                                android.util.Pair.create((View)img3, "shareimg3"),
                                android.util.Pair.create((View)img4, "shareimg4"))
                                .toBundle());
                break;
            case R.id.img4://图片浏览
                ArrayList<String> urls = new ArrayList<>();
                urls.add("https://picsum.photos/id/144/500/600");
                urls.add("https://picsum.photos/id/263/500/600");
                urls.add("https://picsum.photos/id/322/500/600");
                urls.add("https://picsum.photos/id/362/500/600");
                Activity_ImageBrowse.startActivity(this,urls,0);
                break;
            case R.id.huaru://滑入
                startActivitySlide(ShareActivity.class);
                break;
            case R.id.bt://揭露
                startActivityJieLu(JieLu.class, bt);
                break;
            case R.id.lottie:
                startActivitySlide(Lottie.class);
                break;
            case R.id.xuanfu:
                startActivitySlide(XuanFu.class);
                break;
        }
    }
    @Override
    protected void initView() {
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);
        img6 = (ImageView) findViewById(R.id.img6);
        huaru = (ImageView) findViewById(R.id.huaru);
        bt = (FloatingActionButton) findViewById(R.id.bt);
        lottie = (Button) findViewById(R.id.lottie);
        xuanfu = (Button) findViewById(R.id.xuanfu);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);
        huaru.setOnClickListener(this);
        bt.setOnClickListener(this);
        lottie.setOnClickListener(this);
        xuanfu.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        Glide.with(mContext)
                .load("https://picsum.photos/id/641/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img1);
        Glide.with(mContext)
                .load("https://picsum.photos/id/642/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img2);
        Glide.with(mContext)
                .load("https://picsum.photos/id/611/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img3);
        Glide.with(mContext)
                .load("https://picsum.photos/id/241/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img4);
        Glide.with(mContext)
                .load("https://picsum.photos/id/612/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img5);
        Glide.with(mContext)
                .load("https://picsum.photos/id/225/500/600")
                .apply(new RequestOptions()
                        .dontAnimate()
                        .centerCrop()
                )
                .into(img6);
    }


}
