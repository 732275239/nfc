package com.chuyu.nfc.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chuyu.nfc.R;
import com.chuyu.nfc.adapter.RecordingAdapter;
import com.chuyu.nfc.base.BaseFragment;
import com.chuyu.nfc.cusview.recyclerview.SimpleItemDecoration;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.ui.MainActivity;


public class MainFragment2 extends BaseFragment {
    private RecyclerView recyclerView;
    private RecordingAdapter adapter;
    private LinearLayout layout;


    public static MainFragment2 newInstance() {
        Bundle args = new Bundle();
        MainFragment2 fragment = new MainFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case EventCode.CODE4:

                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_home1;
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
        recyclerView = rootView.findViewById(R.id.recyclerview);
        layout = rootView.findViewById(R.id.layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleItemDecoration(getActivity(), 0));
        adapter = new RecordingAdapter(getActivity(), MainActivity.v1);
        recyclerView.setAdapter(adapter);
        if (MainActivity.v1.size() == 0) {
            layout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            adapter.notifyDataSetChanged();
            if (MainActivity.v1.size() == 0) {
                layout.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                layout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }


    //网络数据
    @Override
    protected void initDatas() {


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
