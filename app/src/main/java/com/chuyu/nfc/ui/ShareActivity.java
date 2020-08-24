package com.chuyu.nfc.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;

/**
 * Created by Zoello on 2019/5/23.
 */

public class ShareActivity extends BaseActivity{
    private ImageView img1;
    private ImageView img3;
    private ImageView img2;
    private ImageView img4;

    @Override
    protected boolean isRegisterEventBusHere() {
        return false;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.shareactivity;
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
        img1 = (ImageView) findViewById(R.id.img1);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img2 = (ImageView) findViewById(R.id.img2);
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

    }

    @Override
    public void onClick(View v) {

    }
}
