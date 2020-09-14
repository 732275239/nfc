package com.chuyu.nfc.ui;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.bean.MainTabBean;
import com.chuyu.nfc.bean.company;
import com.chuyu.nfc.bean.nfcRecording;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class MainActivity extends BaseActivity {
    public static int currentNavigationBarStatus = -1;//当前Tab页面索引
    private LinearLayout navigation_bar;
    private List<MainTabBean> mainTabList;
    private RelativeLayout navigationButtonMain1;
    private RelativeLayout navigationButtonMain2;
    private RelativeLayout navigationButtonMain3;
    private long exitTime;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    public static Map<String, company> uid = new HashMap<>();
    public static List<nfcRecording> v1 = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = mainTabList.get(0).getTabFragment();
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState); 阻止activity保存fragment的状态,解决Fragment穿透重叠现象
    }

    private void createTabData() {
        mainTabList = new ArrayList<MainTabBean>();
        mainTabList.add(new MainTabBean(0, R.mipmap.nfc2, R.mipmap.nfc1, "nfc"));
        mainTabList.add(new MainTabBean(1, R.mipmap.jilv2, R.mipmap.jilv1, "记录"));
        mainTabList.add(new MainTabBean(2, R.mipmap.tj2, R.mipmap.tj1, "统计"));
    }

    @Override
    protected void initView() {
        initTab();
        uid.put("04414e3a226881", new company("武汉楚誉科技股份有限公司", "电梯口消防栓"));
        uid.put("0400d14a226881", new company("武汉楚誉科技股份有限公司", "会议室消防栓"));
        uid.put("0459d04a226880", new company("武汉丽思卡尔顿酒店", "一楼消防栓"));
        uid.put("0451ab32ca6681", new company("武汉丽思卡尔顿酒店", "二楼消防栓"));
        uid.put("04186d3a226881", new company("武汉丽思卡尔顿酒店", "三楼消防栓"));
        uid.put("044ed14a226881", new company("武汉丽思卡尔顿酒店", "四楼消防栓"));
    }

    //创建底部tab
    public void initTab() {
        currentNavigationBarStatus = -1;
        createTabData();
        LinearLayout navigation_container = (LinearLayout) findViewById(R.id.activity_main_navigation_bar);
        navigationButtonMain1 = (RelativeLayout) findViewById(R.id.navigation_button_main_1);
        navigationButtonMain2 = (RelativeLayout) findViewById(R.id.navigation_button_main_2);
        navigationButtonMain3 = (RelativeLayout) findViewById(R.id.navigation_button_main_3);
        if (mainTabList.size() <= 1) {
            navigation_container.setVisibility(View.GONE);
        } else {
            navigation_container.setVisibility(View.VISIBLE);
        }
        navigation_bar = (LinearLayout) navigation_container.getChildAt(1);
        changeNavigationBar(0);
    }

    @Override
    protected void initDatas() {

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        if (nfcAdapter == null) {
            Toast.makeText(MainActivity.this, "设备不支持NFC", Toast.LENGTH_LONG).show();
            return;
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            Toast.makeText(MainActivity.this, "请在系统设置中先启用NFC功能", Toast.LENGTH_LONG).show();
            return;
        }
        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resolveIntent(intent);
    }

    void resolveIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            processTag(intent);
        }
    }

    public void processTag(Intent intent) {//处理tag
        String uid = "";
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] aa = tagFromIntent.getId();
        uid += bytesToHexString(aa);//获取卡的UID
        EventBus.getDefault().post(new EventCenter(EventCode.CODE1, uid));
        navigationButtonMain1.performClick();
    }

    //字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                    null, null);
    }

    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case EventCode.CODE2:
                EventBus.getDefault().post(new EventCenter(EventCode.CODE4, ""));
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.navigation_button_main_1:
                if (currentNavigationBarStatus != 0) {
                    JUMP(navigationButtonMain1);
                    changeNavigationBar(0);
                }

                break;
            case R.id.navigation_button_main_2:
                if (currentNavigationBarStatus != 1) {
                    JUMP(navigationButtonMain2);
                    changeNavigationBar(1);
                }

                break;
            case R.id.navigation_button_main_3:
                if (currentNavigationBarStatus != 2) {
                    JUMP(navigationButtonMain3);
                    changeNavigationBar(2);
                }

                break;
        }
    }


    /**
     * 切换导航栏
     *
     * @param i 要切换的页面索引
     */
    public void changeNavigationBar(int i) {
        currentNavigationBarStatus = i;
        changeNavigationTabBtn(i);
        try {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            MainTabBean mainTabBean = mainTabList.get(i);
            Fragment showFragment = mainTabBean.getTabFragment();

            if (showFragment == null) {
                mainTabBean.createFragment();
                showFragment = mainTabBean.getTabFragment();
                mainTabList.set(i, mainTabBean);
                ft.add(R.id.activity_main_frameLayout, showFragment, showFragment.getId() + "");
            }

            for (int i1 = 0; i1 < mainTabList.size(); i1++) {
                if (mainTabList.get(i1).getTabFragment() != null) {
                    if (i1 == i) {
                        ft.show(mainTabList.get(i1).getTabFragment());
                    } else {
                        ft.hide(mainTabList.get(i1).getTabFragment());
                    }

                }
            }
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeNavigationTabBtn(int i) {
        try {
            RelativeLayout navigationButton;
            for (int j = 0; j < mainTabList.size(); j++) {
                MainTabBean mainTabBean = mainTabList.get(j);
                navigationButton = (RelativeLayout) navigation_bar.getChildAt(j);
                ((TextView) navigationButton.getChildAt(0)).setText(mainTabBean.getTitle());
                if (i == j) {
                    ((TextView) navigationButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.navigation_bar_tex_tv));
                    ((ImageView) navigationButton.getChildAt(1)).setImageResource(mainTabBean.getTabSelectIcon());
                } else {
                    ((TextView) navigationButton.getChildAt(0)).setTextColor(getResources().getColor(R.color.navigation_bar_main_tv));
                    ((ImageView) navigationButton.getChildAt(1)).setImageResource(mainTabBean.getTabIcon());
                }
                navigationButton.setVisibility(View.VISIBLE);
                navigationButton.setOnClickListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                // 判断2次点击事件时间
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    ToastUtil.showTips(this, "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    finish();
                }
            }
            return true;
        }
//        return super.dispatchKeyEvent(event);
        return false;
    }

    private void JUMP(final View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.main_tab_icon_animation));

    }

}
