package com.chuyu.nfc.bean;


import android.app.Fragment;

import com.chuyu.nfc.fragment.MainFragment1;
import com.chuyu.nfc.fragment.MainFragment2;
import com.chuyu.nfc.fragment.MainFragment3;

/**
 * Created by Administrator on 2018/9/27 0027.
 */

public class MainTabBean implements java.io.Serializable {

    private int tabIndex;
    private String title;
    private Fragment tabFragment = null;
    private int tabIcon;
    private int tabSelectIcon;

    public MainTabBean(int tabIndex, int tabIcon, int tabSelectIcon, String title) {
        this.tabIndex = tabIndex;
        this.tabIcon = tabIcon;
        this.tabSelectIcon = tabSelectIcon;
        this.title = title;
        tabFragment = null;
    }

    /**
     * 需要时创建
     */
    public void createFragment() {
        if (tabFragment != null) {
            return;
        }
        switch (tabIndex) {
            case 0:
                tabFragment = MainFragment1.newInstance();
                break;
            case 1:
                tabFragment = MainFragment2.newInstance();
                break;
            case 2:
                tabFragment = MainFragment3.newInstance();
                break;
        }
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getTabFragment() {
        return tabFragment;
    }

    public void setTabFragment(Fragment tabFragment) {
        this.tabFragment = tabFragment;
    }

    public int getTabIcon() {
        return tabIcon;
    }

    public void setTabIcon(int tabIcon) {
        this.tabIcon = tabIcon;
    }

    public int getTabSelectIcon() {
        return tabSelectIcon;
    }

    public void setTabSelectIcon(int tabSelectIcon) {
        this.tabSelectIcon = tabSelectIcon;
    }

}
