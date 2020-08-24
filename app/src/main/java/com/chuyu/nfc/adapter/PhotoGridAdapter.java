package com.chuyu.nfc.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chuyu.nfc.R;

import java.util.ArrayList;

/**
 * Created by Zoello on 2018/11/26.
 */

public class PhotoGridAdapter extends BaseAdapter {
    private ArrayList<String> listUrls;
    private LayoutInflater inflater;
    private int imgsize;
    private int imgwidth;
    private Activity activity;

    public PhotoGridAdapter(ArrayList<String> listUrls, int imgsize, int imgwidth, Activity activity) {
        this.activity = activity;
        this.imgwidth = imgwidth;
        this.listUrls = listUrls;
        this.imgsize = imgsize;
        if (listUrls.size() == imgsize + 1) { //如果是10张 就说明加号跑到集合里面
            listUrls.remove(listUrls.size() - 1);
        }
        inflater = LayoutInflater.from(activity);
    }

    public int getCount() {
        return listUrls.size();
    }

    @Override
    public String getItem(int position) {
        return listUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置图片高度
        ViewGroup.LayoutParams layoutParams = holder.image.getLayoutParams();
        layoutParams.width = imgwidth;
        layoutParams.height = imgwidth;
        holder.image.setLayoutParams(layoutParams);

        final String path = listUrls.get(position);
        if (path.equals("000000")) {
            if (position > imgsize - 1) { //大于8张 隐藏加号按钮
                holder.image.setVisibility(View.GONE);
            } else {
                holder.image.setImageResource(R.drawable.camera3);
            }
        } else {
            Glide.with(activity)
                    .load(path)
                    .apply(new RequestOptions()
                            .dontAnimate()
                            .centerCrop()
                    )
                    .into(holder.image);

        }


        return convertView;
    }

    class ViewHolder {
        ImageView image;
    }}

