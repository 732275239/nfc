package com.chuyu.nfc.tools;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chuyu.nfc.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GridIMG {
    public static void setGridimg(Context context, GridLayout layout, ArrayList<String> urlList) {
        layout.removeAllViews();
        int i1 = DensityUtils.dp2px(context, 30);
        int width = (DensityUtils.getScreenWidth(context) - i1) / 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        final List<ImageView> imgs = new ArrayList<>();
        imgs.clear();
        int i = 0;
        for (String url : urlList) {
            View view = View.inflate(context, R.layout.view_img, null);
            ImageView img = (ImageView) view.findViewById(R.id.img);
            Glide.with(context).load(new File(url))
                    .into(img);

            img.setLayoutParams(lp);
            imgs.add(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (layout != null) {
                layout.addView(view);
            }
            i++;
        }
    }
}
