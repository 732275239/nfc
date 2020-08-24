package com.chuyu.nfc.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.cusview.recyclerview.BaseViewHolder;
import com.chuyu.nfc.cusview.recyclerview.LoadMoreAdapter;

import java.util.ArrayList;

/**
 */

public class TestAdapter extends LoadMoreAdapter<String> {
    private Activity context;

    public TestAdapter(Activity context, ArrayList<String> mData) {
        super(context, R.layout.test_layout_item, mData);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final String item, int index) {
        if (item == null) {
            return;
        }
        TextView name = holder.getView(R.id.name);//姓名
        name.setText(item);
    }

}
