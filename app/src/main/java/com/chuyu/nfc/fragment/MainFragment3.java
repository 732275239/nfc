package com.chuyu.nfc.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseFragment;
import com.chuyu.nfc.bean.nfcRecording;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.ui.MainActivity;


public class MainFragment3 extends BaseFragment {
    private TextView b1;
    private TextView b2;

    public static MainFragment3 newInstance() {
        Bundle args = new Bundle();
        MainFragment3 fragment = new MainFragment3();
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
//            case EventCode.CODE1:
//                if ((eventCenter.getData()).equals("Refresh")) {
//                    scrollView.fullScroll(View.FOCUS_UP);
//                    refresh.beginRefreshing();//代码调用下拉刷新
//                    //需要刷新页面了
//                }
//                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_home3_page;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
        //返回需要加载的页面，一般用于设置条目布局，加载空数据重试等等操
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }

    @Override
    protected void initView() {
        b1 = rootView.findViewById(R.id.b1);
        b2 = rootView.findViewById(R.id.b2);

    }

    private float company1 = 0;
    private float company2 = 0;

    //网络数据
    @Override
    protected void initDatas() {
        company1 = 0;
        company2 = 0;
        for (nfcRecording nfcRecording : MainActivity.v1) {
            String name = nfcRecording.getName();
            if (name.contains("楚誉")) {
                company1 = company1 + 1;
            }
            if (name.contains("丽思")) {
                company2 = company2 + 1;
            }
        }
        float i = (company1 / 2) * 100;
        b1.setText("检查百分比" + (int) i + "%");
        float i1 = (company2 / 4) * 100;
        b2.setText("检查百分比" + (int) i1 + "%");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        company1 = 0;
        company2 = 0;
        for (nfcRecording nfcRecording : MainActivity.v1) {
            String name = nfcRecording.getName();
            if (name.contains("楚誉")) {
                company1 = company1 + 1;
            }
            if (name.contains("丽思")) {
                company2 = company2 + 1;
            }
        }
        Log.e("abc", company1 + "--" + company2);
        float i = (company1 / 2) * 100;
        b1.setText("检查百分比" + (int) i + "%");
        float i1 = (company2 / 4) * 100;
        b2.setText("检查百分比" + (int) i1 + "%");
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                break;

        }
    }

}
