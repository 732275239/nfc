package com.chuyu.nfc.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.bean.nfcRecording;
import com.chuyu.nfc.cusview.recyclerview.BaseViewHolder;
import com.chuyu.nfc.cusview.recyclerview.LoadMoreAdapter;
import com.chuyu.nfc.tools.GridIMG;

import java.util.ArrayList;
import java.util.List;


public class RecordingAdapter extends LoadMoreAdapter<nfcRecording> {
    private Activity context;

    public RecordingAdapter(Activity context, List<nfcRecording> mData) {
        super(context, R.layout.recor_layout_item, mData);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final nfcRecording item, int index) {
        if (item == null) {
            return;
        }

        TextView title = holder.getView(R.id.title);
        GridLayout grid_layout = holder.getView(R.id.grid_layout);
        TextView 备注 = holder.getView(R.id.备注);
        String[] split = item.getUrls().split(",");
        ArrayList<String> imgs = new ArrayList<>();
        imgs.clear();
        for (int i = 0; i < split.length; i++) {
            imgs.add(split[i]);
        }

        if (imgs.size() > 0) {
            grid_layout.setVisibility(View.VISIBLE);
        } else {
            grid_layout.setVisibility(View.GONE);
        }

        GridIMG.setGridimg(mContext, grid_layout, imgs);
        title.setText(item.getName());
        备注.setText(item.getRemark());
    }

}
