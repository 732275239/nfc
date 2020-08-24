package com.chuyu.nfc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chuyu.nfc.R;
import com.chuyu.nfc.cusview.PhotoViewPager;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by 赵政
 * 使用
 * <p>
 * ArrayList<String> urls = new ArrayList<>();
 * for (int i = 0; i < urlList.size(); i++) {
 * urls.add(urlList.get(i) + "");
 * }
 * Intent intent = new Intent(mcontext, Activity_ImageBrowse.class);
 * intent.putExtra("position", finalI);
 * intent.putStringArrayListExtra("imgs", urls);
 * mcontext.startActivity(intent);
 */

public class Activity_ImageBrowse extends Activity {
    private List<String> imgs;
    private int position;
    private TextView tv;
    private PhotoViewPager viewpager;
    private LinearLayout view;
    private ImageView imgBack;
    private ImageView bg;


    public static void startActivity(Activity context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, Activity_ImageBrowse.class);
        intent.putExtra("position", index);
        intent.putStringArrayListExtra("imgs", urls);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
        this.position = getIntent().getIntExtra("position", 0);
        this.imgs = getIntent().getStringArrayListExtra("imgs");
        viewpager = (PhotoViewPager) findViewById(R.id.imgs_viewpager);
        imgBack = (ImageView) findViewById(R.id.img_back);//返回
        tv = (TextView) this.findViewById(R.id.tv);//显示图片索引

        bg = (ImageView) findViewById(R.id.bg);

        viewpager.setOffscreenPageLimit(1);
        PagerAdapter adapter = new MyViewPagerAdapter(this, imgs);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(position);
        //滑动的设置数据
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv.setText(position + 1 + "/" + imgs.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //高斯模糊

//        Glide.with(Activity_ImageBrowse.this)
//                .load(imgs.get(0))
//                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(Activity_ImageBrowse.this)))
//                .into(bg);
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        boolean isfash = true;
        List<String> imgs;
        Context mContext;

        public MyViewPagerAdapter(Context context, List<String> imgs) {
            this.mContext = context;
            this.imgs = imgs;
        }

        @Override
        public int getCount() { // 获得size
            return imgs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if (isfash) {
                tv.setText(position + 1 + "/" + imgs.size());
                isfash = false;
            }
            view = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.img_browse, null);
            final PhotoView img = (PhotoView) view.findViewById(R.id.img_plan);
            final ImageView loading = (ImageView) view.findViewById(R.id.img);
            img.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            String imgUrl = imgs.get(position);
            Glide.with(Activity_ImageBrowse.this)
                    .load(imgUrl)
                    .apply(new RequestOptions()
                            .centerInside()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            img.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            img.setImageDrawable(resource);
                        }
                    });
            ((ViewPager) container).addView(view);

            img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }
            });

            return view;
        }
    }
}
