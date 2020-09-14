package com.chuyu.nfc.base;

import android.app.Application;
import android.content.Context;

import com.chuyu.nfc.tools.share.SharedPreferencesTools;
import com.chuyu.nfc.upgrade.sdk.UpgradeApp;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cache.DBCacheStore;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;



/**
 * 名称：Application
 * 创建时间：2017/4/13 上午 9:56
 * 详细说明：
 */
public class ApplicationContext extends Application {
    private static ApplicationContext instance;
    private SharedPreferencesTools spTools;

    public static int WEB_LAYER;

    public SharedPreferencesTools getSpTools() {
        if (spTools == null) {
            spTools = new SharedPreferencesTools(instance);
        }
        return spTools;
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Context getAppContext(){
        return this.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initNet();//初始化网络框架
        spTools = getSpTools();// 初始化工具
//        initX5();
//        initNewX5();
//        initLeakcanary();//初始化内存检测
//        initHa();


        UpgradeApp.registUpload(this);
    }


    private void initNet() {
        try{
            // 如果你需要自定义配置：
            NoHttp.initialize(this, new NoHttp.Config()
                    // 设置全局连接超时时间，单位毫秒，默认10s。
                    .setConnectTimeout(30 * 1000)
                    // 设置全局服务器响应超时时间，单位毫秒，默认10s。
                    .setReadTimeout(30 * 1000)
                    // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                    .setCacheStore(
                            new DBCacheStore(this).setEnable(false) // 如果不使用缓存，设置false禁用。
                    )
                    // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                    .setCookieStore(
                            new DBCookieStore(this).setEnable(false) // 如果不维护cookie，设置false禁用。
                    )
                    // 使用OkHttp
                    .setNetworkExecutor(new OkHttpNetworkExecutor())
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
