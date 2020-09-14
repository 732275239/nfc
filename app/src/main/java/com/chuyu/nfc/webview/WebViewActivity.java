package com.chuyu.nfc.webview;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.baoyz.actionsheet.ActionSheet;
import com.bumptech.glide.Glide;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.BaseAppManager;
import com.chuyu.nfc.base.Constants;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.UploadImgResult;
import com.chuyu.nfc.bean.WebHandleBean;
import com.chuyu.nfc.cusview.materialrefresh.MaterialRefreshLayout;
import com.chuyu.nfc.cusview.materialrefresh.MaterialRefreshListener;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.ParamUtil;
import com.chuyu.nfc.listening.ConfirmDialogOkListening;
import com.chuyu.nfc.listening.CoreWebViewListening;
import com.chuyu.nfc.listening.KeyBoardListener;
import com.chuyu.nfc.listening.PayCallBackListening;
import com.chuyu.nfc.listening.UpRoundHandleListening;
import com.chuyu.nfc.listening.UploadImgHandleListening;
import com.chuyu.nfc.tools.AlertUtils;
import com.chuyu.nfc.tools.AssetsUtils;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.JsonUtil;
import com.chuyu.nfc.tools.NetUtil;
import com.chuyu.nfc.tools.PayResult;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.tools.Tools;
import com.chuyu.nfc.ui.ScanCodeActivity;
import com.chuyu.nfc.wxapi.WXShare;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;
import com.yuyh.library.imgsel.config.ISCameraConfig;
import com.yuyh.library.imgsel.config.ISListConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.picker.TimePicker;
import cn.qqtheme.framework.util.StorageUtils;
import de.greenrobot.event.EventBus;
import me.shaohui.bottomdialog.BottomDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import wendu.dsbridge.CompletionHandler;

import static com.chuyu.nfc.constant.Constants.WX_APP_ID;

/**
 * Created by Administrator on 2018/9/27 0027.
 */

public class WebViewActivity extends BaseActivity implements CoreWebViewListening {


    private RelativeLayout top_bar;
    private ImageView top_bar_backImg;
    private TextView top_bar_titleTv;
    public ImageView top_bar_rightImg;
    public TextView top_bar_rightTv;
    private MaterialRefreshLayout coreWebviewBody;
    private CoreWebView coreWebView;
    private String title;
    private String loadingUrl;
    private boolean hasLoading = true;
    private WebViewHandler webViewHandler = new WebViewHandler();
    private String call_right_txt_fun;
    private String call_right_img_fun;
    private String call_scan_fun;
    private String call_pull_fun;
    private String call_up_fun;
    private ProgressDialog loadingDialog;
    private ProgressBar progresss;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private UploadImgHandleListening uploadImgHandleListening;
    private UpRoundHandleListening upRoundHandleListening;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_webview;
    }

    @Override
    protected View getLoadingTargetView() {

        return findViewById(R.id.core_webview_layout);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        title = extras.getString(Constants.WEBTITLE);
        loadingUrl = extras.getString(Constants.WEBURL);
        hasLoading = extras.getBoolean(Constants.WEB_HAS_LOAD);
    }

    @Override
    protected void initView() {
        initJsCall();
        progresss = (ProgressBar) findViewById(R.id.progress);
        top_bar = (RelativeLayout) findViewById(R.id.top_bar);
        top_bar_backImg = (ImageView) findViewById(R.id.top_bar_leftImg);
        top_bar_titleTv = (TextView) findViewById(R.id.top_bar_titleTv);
        top_bar_rightImg = (ImageView) findViewById(R.id.top_bar_rightImg);
        top_bar_rightTv = (TextView) findViewById(R.id.top_bar_rightTv);

        coreWebviewBody = (MaterialRefreshLayout) findViewById(R.id.core_webview_layout);
        try {
            coreWebviewBody.removeAllViews();
            CoreWebParamBean coreWebParamBean = new CoreWebParamBean();
            coreWebParamBean.setHasLoading(hasLoading);
            coreWebView = CoreWebView.getInstance(this, webViewHandler, this, coreWebParamBean);
            coreWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            coreWebviewBody.addView(coreWebView, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        coreWebviewBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if (coreWebView == null) {
                    return;
                }
                if (NetUtil.isNetwork(WebViewActivity.this)) {
                    coreWebView.executeJavaScript(call_pull_fun);
                } else {
                    try {
                        coreWebviewBody.finishRefresh();
                    } catch (Exception e) {
                    }
                    ToastUtil.showTips(WebViewActivity.this, "请检查网络连接！");
                }

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (coreWebView == null) {
                    return;
                }
                if (NetUtil.isNetwork(WebViewActivity.this)) {
                    coreWebView.executeJavaScript(call_up_fun);
                } else {

                    try {
                        coreWebviewBody.finishRefreshLoadMore();
                    } catch (Exception e) {
                    }
                    ToastUtil.showTips(WebViewActivity.this, "请检查网络连接！");
                }

            }
        });
        top_bar_backImg.setVisibility(View.VISIBLE);
        top_bar_rightImg.setVisibility(View.GONE);
        top_bar_rightTv.setVisibility(View.GONE);
        top_bar_backImg.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            top_bar_titleTv.setText(title);
        } else {
            top_bar_titleTv.setText("");
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void initJsCall() {
        call_scan_fun = "";
        call_right_txt_fun = "";
        call_right_img_fun = "";
        call_pull_fun = "";
        call_up_fun = "";
    }

    private void initMapLoaction(final CompletionHandler<String> completionHandler) {
        try {
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();

            mlocationClient.setLocationListener(new AMapLocationListener() {

                @Override
                public void onLocationChanged(AMapLocation amapLocation) {
                    if (amapLocation != null) {
                        if (amapLocation.getErrorCode() == 0) {
                            //定位成功回调信息，设置相关消息
//						amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//						amapLocation.getLatitude();//获取纬度
//						amapLocation.getLongitude();//获取经度
//						amapLocation.getAccuracy();//获取精度信息
//						amapLocation.getCountry();//国家信息
//						amapLocation.getProvince();//省信息
//						amapLocation.getCity();//城市信息
//						amapLocation.getDistrict();//城区信息
//						amapLocation.getStreet();//街道信息
//						amapLocation.getStreetNum();//街道门牌号信息
//						amapLocation.getCityCode();//城市编码
//						amapLocation.getAdCode();//地区编码
                            String address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                            Map<String, String> longlat = new HashMap<>();
                            longlat.put("long", amapLocation.getLongitude() + "");
                            longlat.put("lat", amapLocation.getLatitude() + "");
                            longlat.put("address", address);
                            String s = JsonUtil.mapToJson(longlat);
                            completionHandler.complete(s);

                            if (mlocationClient != null) {
                                //销毁定位客户端。
                                mlocationClient.onDestroy();
                            }
                        } else {
                            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        }
                    }
                }
            });
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //定位一次
//            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startMapLocation() {
        try {
            if (mlocationClient == null) {
                return;
            }
            //启动定位
            mlocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startAct(Activity paramAct, String title, String loadingUrl) {
        Intent intent = new Intent(paramAct, WebViewActivity.class);
        intent.putExtra(Constants.WEBTITLE, title);
        intent.putExtra(Constants.WEBURL, loadingUrl);
        intent.putExtra(Constants.WEB_HAS_LOAD, true);
        paramAct.startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    public static void startAct(Activity paramAct, String title, String loadingUrl, boolean hasLoading) {
        Intent intent = new Intent(paramAct, WebViewActivity.class);
        intent.putExtra(Constants.WEBTITLE, title);
        intent.putExtra(Constants.WEBURL, loadingUrl);
        intent.putExtra(Constants.WEB_HAS_LOAD, hasLoading);
        paramAct.startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_leftImg:
                boolean b = coreWebView.canGoBack();
                if (!b){
                    finish();
                }else {
                    coreWebView.goBack();
                }
                break;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean b = coreWebView.canGoBack();
            if (!b){
                finish();
            }else {
                coreWebView.goBack();
            }
            return true;
        }
        return false;
    }
    private WXShare wxShare;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxShare = new WXShare(WebViewActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();
        wxShare.register();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        initJsCall();
        wxShare.unregister();
//        if (coreWebView != null) {
//            coreWebView.destroy();
//        }
    }

    @Override
    protected void initDatas() {

        KeyBoardListener.getInstance(this).init();
        if (loadingUrl.startsWith("/")) {
            loadingUrl = URLs.H5_URL + loadingUrl;
        }
        coreWebView.initData(loadingUrl);
        coreWebView.setOnProgress(new CoreWebView.onProgress() {
            @Override
            public void onProgress(int abc) {
                progresss.setVisibility(View.VISIBLE);
                progresss.setProgress(abc);
                if (abc == 100) {
                    progresss.setVisibility(View.GONE);
                    progresss.setProgress(0);
                }
            }
        });
    }

    private class WebViewHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }
            try {

                Object msgObj = msg.obj;

                if (msgObj == null) {
                    return;
                }

                WebHandleBean webHandleBean = (WebHandleBean) msgObj;

                int event = msg.what;

                switch (event) {
//                    final ShareDialog dialog = new ShareDialog(getActivity());
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                    dialog.show();
//                    dialog.setOnWindowItemClickListener(new ShareDialog.OnWindowItemClickListener() {
//                        @Override
//                        public void shareQQ() {
//                            dialog.cancel();
//                        }
//
//                        @Override
//                        public void shareWechat() {
//                            dialog.cancel();
//                        }
//
//                        @Override
//                        public void shareCircle() {
//                            dialog.cancel();
//                        }
//
//                    });
                    case Constants.EVENT_16://退出登录
                        EventBus.getDefault().post(new EventCenter(EventCode.CODE3, "首页"));
                        ToastUtil.showTips(WebViewActivity.this, "已退出登录");
                        finish();
                        break;
                    case Constants.EVENT_00:
                        WebViewActivity.startAct(WebViewActivity.this, webHandleBean.getTitle(), webHandleBean.getUrl());
                        break;
                    case Constants.EVENT_01:
                        if (webHandleBean.getTag() == 0) {
                            coreWebView.loadData();
                        } else {
                            loadingUrl = webHandleBean.getUrl();
                            initDatas();
                        }
                        break;
                    case Constants.EVENT_02:
                        if (webHandleBean.getTag() == 0) {
                            call_right_txt_fun = webHandleBean.getCall();
                            top_bar_rightTv.setText(webHandleBean.getParam());
                            top_bar_rightImg.setVisibility(View.GONE);
                            top_bar_rightTv.setVisibility(View.VISIBLE);
                        } else {
                            call_right_img_fun = webHandleBean.getCall();
                            Glide.with(WebViewActivity.this).load(webHandleBean.getParam()).into(top_bar_rightImg);
                            top_bar_rightImg.setVisibility(View.VISIBLE);
                            top_bar_rightTv.setVisibility(View.GONE);
                        }
                        break;
                    case Constants.EVENT_03:
                        BaseAppManager.getInstance().backMain();
                        break;
                    case Constants.EVENT_04:
                        if (webHandleBean.getTag() == 0) {
                            call_right_txt_fun = "";
                            call_right_img_fun = "";
                            top_bar_rightImg.setVisibility(View.GONE);
                            top_bar_rightTv.setVisibility(View.GONE);
                        } else if (webHandleBean.getTag() == 1) {
                            top_bar_backImg.setVisibility(View.GONE);
                        } else if (webHandleBean.getTag() == 2) {
                            top_bar_backImg.setVisibility(View.VISIBLE);
                        } else if (webHandleBean.getTag() == 3) {
                            top_bar.setVisibility(View.VISIBLE);
                        } else if (webHandleBean.getTag() == 4) {
                            top_bar.setVisibility(View.GONE);
                        } else if (webHandleBean.getTag() == 5) {
                            try {
                                top_bar.setBackgroundColor(Color.parseColor(webHandleBean.getParam()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        break;
                    case Constants.EVENT_05:
                        if (webHandleBean.getTag() == 0) {
                            openLoading("");
                        } else if (webHandleBean.getTag() == 1) {
                            openLoading("");
                        } else if (webHandleBean.getTag() == 2) {
                            startLoading(webHandleBean.getParam());
                        } else if (webHandleBean.getTag() == 3) {
                            closeLoading();
                        }
                        break;
                    case Constants.EVENT_06:
                        final String callF = webHandleBean.getCall();
                        AlertUtils.confirmDialog(WebViewActivity.this, webHandleBean.getTitle(), webHandleBean.getParam(), new ConfirmDialogOkListening() {
                            @Override
                            public void clickDialogOk() {
                                coreWebView.executeJavaScript(callF);
                            }
                        });
                        break;
                    case Constants.EVENT_07:
                        List<String> paramList = new ArrayList<String>();
                        paramList.add(webHandleBean.getCall());
                        call_scan_fun = webHandleBean.getCall();
                        new IntentIntegrator(WebViewActivity.this)
                                .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                                .setPrompt("请对准二维码")// 设置提示语
                                .setCameraId(0)// 选择摄像头,可使用前置或者后置
                                .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                                .setBarcodeImageEnabled(true)// 扫完码之后生成二维码的图片
                                .setCaptureActivity(ScanCodeActivity.class)
                                .setDesiredBarcodeFormats(paramList)
                                .initiateScan();// 初始化扫码
                        break;

                    case Constants.EVENT_09:
                        if (webHandleBean.getTag() == 0) {
                            call_pull_fun = webHandleBean.getCall();
                            coreWebviewBody.setRefresh(true);
                        } else if (webHandleBean.getTag() == 1) {
                            coreWebviewBody.setLoadMore(true);
                            call_up_fun = webHandleBean.getCall();
                        }
                        break;
                    case Constants.EVENT_10:
                        String dataMode = webHandleBean.getParam();
                        final String callFn = webHandleBean.getCall();
                        if (dataMode.equals("Y-M")) {//年月
                            DatePicker picker = new DatePicker(WebViewActivity.this, DatePicker.YEAR_MONTH);
                            picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
                                @Override
                                public void onDatePicked(String year, String month) {
                                    coreWebView.executeJavaScript(callFn + Tools.createJsBack(year, month));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } else if (dataMode.equals("Y-M-D")) {//年月日
                            DatePicker picker = new DatePicker(WebViewActivity.this, DatePicker.YEAR_MONTH_DAY);
                            picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                                @Override
                                public void onDatePicked(String year, String month, String day) {
                                    coreWebView.executeJavaScript(callFn + Tools.createJsBack(year, month, day));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } else if (dataMode.equals("Y-M-D H-M")) {//年月日 时分
                            DateTimePicker picker = new DateTimePicker(WebViewActivity.this, DateTimePicker.HOUR_OF_DAY);
                            picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
                                @Override
                                public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                                    coreWebView.executeJavaScript(callFn + Tools.createJsBack(year, month, day, hour, minute));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } else if (dataMode.equals("M-D")) {//月日
                            DatePicker picker = new DatePicker(WebViewActivity.this, DatePicker.MONTH_DAY);
                            picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
                                @Override
                                public void onDatePicked(String month, String day) {
                                    coreWebView.executeJavaScript(callFn + Tools.createJsBack(month, day));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } else if (dataMode.equals("H-M")) {//时分
                            TimePicker picker = new TimePicker(WebViewActivity.this, TimePicker.HOUR_OF_DAY);
                            picker.setTopLineVisible(false);
                            picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                                @Override
                                public void onTimePicked(String hour, String minute) {
                                    coreWebView.executeJavaScript(callFn + Tools.createJsBack(hour, minute));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        }
                        break;
                    case Constants.EVENT_11:
                        try {
                            final String callFn2 = webHandleBean.getCall();
                            OptionPicker picker = new OptionPicker(WebViewActivity.this, webHandleBean.getParams());
                            //picker.setOffset(2);
                            picker.setSelectedIndex(webHandleBean.getTag());
                            picker.setTextSize(20);
                            picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                                @Override
                                public void onOptionPicked(int index, String item) {
                                    coreWebView.executeJavaScript(callFn2 + Tools.createJsBack(index + "", item));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    case Constants.EVENT_12:
                        try {
                            final String callFn3 = webHandleBean.getCall();
                            ArrayList<Province> data = new ArrayList<Province>();
                            String json = AssetsUtils.readText(WebViewActivity.this, "city.json");
                            data.addAll(JSON.parseArray(json, Province.class));
                            AddressPicker picker = new AddressPicker(WebViewActivity.this, data);


                            String[] cArray = webHandleBean.getParams();

                            if (webHandleBean.getParam().equals("city")) {
                                picker.setSelectedItem(cArray[0], cArray[1], "");
                                picker.setHideCounty(true);
                            } else if (webHandleBean.getParam().equals("all")) {
                                picker.setSelectedItem(cArray[0], cArray[1], cArray[2]);
                            } else if (webHandleBean.getParam().equals("area")) {
                                picker.setHideProvince(true);
                                picker.setSelectedItem(cArray[0], cArray[0], cArray[1]);
                            }

                            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                                @Override
                                public void onAddressPicked(Province province, City city, County county) {
                                    coreWebView.executeJavaScript(callFn3 + Tools.createJsBack(province.getName(), city.getName(), county.getName()));
                                }
                            });
                            picker.setAnimationStyle(R.style.Animation_CustomPopup);
                            picker.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constants.EVENT_13:
                        try {
                            final String callFn4 = webHandleBean.getCall();
                            if (webHandleBean.getTag() == 0) {
                                FilePicker picker = new FilePicker(WebViewActivity.this, FilePicker.FILE);
                                picker.setShowHideDir(false);
                                picker.setRootPath(StorageUtils.getInternalRootPath(WebViewActivity.this) + "Download/");
                                //picker.setAllowExtensions(new String[]{".apk"});
                                picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                                    @Override
                                    public void onFilePicked(String currentPath) {
                                        coreWebView.executeJavaScript(callFn4 + Tools.createJsBack(currentPath));

                                    }
                                });
                                picker.setAnimationStyle(R.style.Animation_CustomPopup);
                                picker.show();
                            } else {
                                FilePicker picker = new FilePicker(WebViewActivity.this, FilePicker.DIRECTORY);
                                picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                                    @Override
                                    public void onFilePicked(String currentPath) {
                                        coreWebView.executeJavaScript(callFn4 + Tools.createJsBack(currentPath));

                                    }
                                });
                                picker.setAnimationStyle(R.style.Animation_CustomPopup);
                                picker.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case Constants.EVENT_14:
                        final String callFn5 = webHandleBean.getCall();
                        final String[] items = webHandleBean.getParams();
                        ActionSheet.createBuilder(WebViewActivity.this, getSupportFragmentManager())
                                .setCancelButtonTitle("取消")
                                .setOtherButtonTitles(items)
                                .setCancelableOnTouchOutside(true)
                                .setListener(new ActionSheet.ActionSheetListener() {
                                    @Override
                                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                                        coreWebView.executeJavaScript(callFn5 + Tools.createJsBack(index + "", items[index]));

                                    }

                                    @Override
                                    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {

                                    }
                                }).show();
                        break;
                    case Constants.EVENT_15:
                        final String callFn6 = webHandleBean.getCall();

                        break;
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public void webTitle(String title) {
        if (TextUtils.isEmpty(this.title)) {
            top_bar_titleTv.setText(title);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle backBundle = null;
        if (data != null) {
            backBundle = data.getExtras();
        }
        switch (requestCode) {
            case Constants.REQUEST_CODE:
                if (data == null) {
                    return;
                }
                if (backBundle == null) {
                    return;
                }
                if (resultCode == Constants.RESPONSE_CODE_1000) {
                    if (coreWebView == null) {
                        return;
                    }
                    String call = backBundle.getString("call");
                    coreWebView.executeJavaScript(call);
                } else if (resultCode == Constants.RESPONSE_CODE_1001) {
                    if (coreWebView == null) {
                        return;
                    }
                    boolean isRefresh = backBundle.getBoolean("isRefresh");
                    if (isRefresh) {
                        coreWebView.loadData();
                    }
                } else if (resultCode == Constants.RESPONSE_CODE_1002) {
                    String callFn = backBundle.getString("callFunction");
                    coreWebView.executeJavaScript(callFn);
                }

                break;
            case Constants.SCAN_RS_CODE:
                try {
                    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (result != null && !TextUtils.isEmpty(result.getContents())) {

                        if (TextUtils.isEmpty(call_scan_fun)) {
                            ToastUtil.showTips(this, result.getContents());
                        } else {
                            coreWebView.executeJavaScript(call_scan_fun + "('" + result.getFormatName() + "','" + result.getContents() + "')");
                        }
                    } else {
                        ToastUtil.showTips(this, "未扫描到信息！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQUEST_CAMERA_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    String path = data.getStringExtra("result"); // 图片地址
//                    uploadImg(path);
                    zipimg(new File(path));
                }
                break;
            case Constants.REQUEST_POTO_LIST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    List<String> pathList = data.getStringArrayListExtra("result");
//                    uploadImg(pathList.get(0));
                    File file = new File(pathList.get(0));
                    zipimg(file);
                }
                break;
            case 3000:
                if (resultCode == RESULT_OK && data != null) {
                    List<String> pathList = data.getStringArrayListExtra("result");
                    File file = new File(pathList.get(0));
                    Luban.with(this)
                            .load(file)
                            .ignoreBy(100)//100K以下图片不压缩
                            .setCompressListener(new OnCompressListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onSuccess(File file) {
                                    upIMG(file);
                                }

                                @Override
                                public void onError(Throwable e) {
                                }
                            }).launch();
                }
                break;
        }
    }

    private void zipimg(File file) {
        Luban.with(this)
                .load(file)
                .ignoreBy(100)//100K以下图片不压缩
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        upAvatar(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }).launch();
    }

    //上传头像
    private void upAvatar(File file) {
        Map<String, Object> paramMap = ParamUtil.init();
        paramMap.put("file", file);
        CallNet.callNetNohttp(ParamUtil.create(URLs.UPLOADFILE, paramMap), new ConnectTask<UploadImgResult>(new TypeToken<UploadImgResult>() {
        }, this) {

            @Override
            public void onSuccess(UploadImgResult rsData, int eCode, String eMsg) {
                super.onSuccess(rsData, eCode, eMsg);
                if (rsData != null) {
                    if (upRoundHandleListening != null) {
                        upRoundHandleListening.upRoundHandel(rsData.getOriginal().getImgCallPath(), rsData.getOriginal().getImgPath());
                    }
                }
            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                super.onFailure(eCode, eMsg);
            }
        });

    }


    //上传图片
    private void upIMG(File file) {
        Map<String, Object> paramMap = ParamUtil.init();
        paramMap.put("file", file);
        CallNet.callNetNohttp(ParamUtil.create(URLs.UPLOADFILE, paramMap), new ConnectTask<UploadImgResult>(new TypeToken<UploadImgResult>() {
        }, this) {

            @Override
            public void onSuccess(UploadImgResult rsData, int eCode, String eMsg) {
                super.onSuccess(rsData, eCode, eMsg);
                if (rsData != null) {
                    if (uploadImgHandleListening != null) {
                        uploadImgHandleListening.uploadImgHandel(rsData.getOriginal().getImgCallPath(), rsData.getOriginal().getImgPath());
                    }
                }

            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                super.onFailure(eCode, eMsg);
            }
        });

    }

    @Override
    public void openLoading(String msg) {

    }

    /**
     * 关闭loading对话框
     */
    @Override
    public void closeLoading() {
        try {
            coreWebviewBody.finishRefresh();
        } catch (Exception e) {
        }
        try {
            coreWebviewBody.finishRefreshLoadMore();
        } catch (Exception e) {
        }

        try {
            if (loadingDialog != null) {
                loadingDialog.cancel();
            }
        } catch (Exception e) {
        }
        closeBgLoading();

    }

    //在当前页面加载一个url
    @Override
    public void loadUrl(String url) {
        loadingUrl = url;
        initDatas();
    }

    //刷新当前页面
    @Override
    public void refreshWin() {
        coreWebView.loadData();
    }

    public void startLoading(String msg) {
        try {
            if (loadingDialog != null && loadingDialog.isShowing()) {
                return;
            }
            loadingDialog = new ProgressDialog(this);
            loadingDialog.setMessage(msg);
            loadingDialog.setCancelable(true);
            loadingDialog.setCanceledOnTouchOutside(false);
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {

    }


    @Override
    public void openBgLoading(String msg) {
        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                toggleShowLoading(true);
            }
        });

    }

    @Override
    public void closeBgLoading() {
        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                restoreLoading();
            }
        });

    }


    @Override
    public void showError() {
        boolean isNetWork = NetUtil.isNetwork(this);
        if (isNetWork) {
            toggleShowError(true, "加载失败，点击重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDatas();
                }
            });
        } else {
            toggleNetworkError(true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDatas();
                }
            });
        }
    }

    @Override
    public void openWin(String url, String title, boolean hasLoad) {
        WebViewActivity.startAct(WebViewActivity.this, title, url, hasLoad);
    }

    //关闭之前的几个页面
    @Override
    public void fishOpenWin(String url, String title, boolean hasLoad, final int size) {
        WebViewActivity.startAct(WebViewActivity.this, title, url, hasLoad);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BaseAppManager.getInstance().clear(size);
            }
        }, 200);
    }

    @Override
    public void showRightTextBtn(final String title, final View.OnClickListener onClickListener) {

        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                top_bar_rightTv.setVisibility(View.VISIBLE);
                top_bar_rightImg.setVisibility(View.GONE);
                top_bar_rightTv.setText(title);
                top_bar_rightTv.setOnClickListener(onClickListener);
            }
        });

    }

    @Override
    public void showRightImgBtn(final String imgUrl, final View.OnClickListener onClickListener) {
        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                Glide.with(WebViewActivity.this).load(imgUrl).into(top_bar_rightImg);
                top_bar_rightImg.setVisibility(View.VISIBLE);
                top_bar_rightTv.setVisibility(View.GONE);
                top_bar_rightImg.setOnClickListener(onClickListener);
            }
        });
    }

    @Override
    public void openUploadImg(UploadImgHandleListening uploadImgHandleListening) {
        this.uploadImgHandleListening = uploadImgHandleListening;
        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        // 自由配置选项
        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.WHITE)
                // “确定”按钮文字颜色
                .btnTextColor(Color.GRAY)
                // 使用沉浸式状态栏
                .statusBarColor(Color.parseColor("#F45422"))
                // 返回图标ResId
                //.backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                .backResId(R.mipmap.ic_back)
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#F45422"))
                .needCrop(false)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                // 最大选择图片数量，默认9
                .maxNum(1)
                .build();

        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(WebViewActivity.this, config, 3000);

    }

    @Override
    public void openRoundImg(UpRoundHandleListening upRoundHandleListening) {
        this.upRoundHandleListening = upRoundHandleListening;
        ActionSheet.createBuilder(WebViewActivity.this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍  照", "选择照片")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        if (index == 0) {
                            //call_photo_fun = callFn6;
                            ISCameraConfig config = new ISCameraConfig.Builder()
                                    .needCrop(true) // 裁剪
                                    .cropSize(1, 1, 500, 500)
                                    .build();
                            ISNav.getInstance().toCameraActivity(WebViewActivity.this, config, Constants.REQUEST_CAMERA_CODE);

                        } else if (index == 1) {
                            //call_chose_picture_fun = callFn6;
                            ISNav.getInstance().init(new ImageLoader() {
                                @Override
                                public void displayImage(Context context, String path, ImageView imageView) {
                                    Glide.with(context).load(path).into(imageView);
                                }
                            });
                            // 自由配置选项
                            ISListConfig config = new ISListConfig.Builder()
                                    // 是否多选, 默认true
                                    .multiSelect(false)
                                    // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                                    .rememberSelected(false)
                                    // “确定”按钮背景色
                                    .btnBgColor(Color.WHITE)
                                    // “确定”按钮文字颜色
                                    .btnTextColor(Color.GRAY)
                                    // 使用沉浸式状态栏
                                    .statusBarColor(Color.parseColor("#F45422"))
                                    // 返回图标ResId
                                    //.backResId(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_mtrl_am_alpha)
                                    .backResId(R.mipmap.ic_back)
                                    // 标题
                                    .title("图片")
                                    // 标题文字颜色
                                    .titleColor(Color.WHITE)
                                    // TitleBar背景色
                                    .titleBgColor(Color.parseColor("#F45422"))
                                    // 裁剪大小。needCrop为true的时候配置
                                    .cropSize(1, 1, 500, 500)
                                    .needCrop(true)
                                    // 第一个是否显示相机，默认true
                                    .needCamera(false)
                                    // 最大选择图片数量，默认9
                                    .maxNum(1)
                                    .build();
                            // 跳转到图片选择器
                            ISNav.getInstance().toListActivity(WebViewActivity.this, config, Constants.REQUEST_POTO_LIST_CODE);
                        }
                    }

                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {

                    }
                }).show();
    }

    @Override
    public void openWeChatPay(final PayReq req, final PayCallBackListening payCallBackListening) {
        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                IWXAPI msgApi = WXAPIFactory.createWXAPI(WebViewActivity.this, null);
                msgApi.registerApp(WX_APP_ID);
                msgApi.sendReq(req);
            }
        });

    }

    @Override
    public void openAlipay(final String paramReq, final PayCallBackListening payCallBackListening) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(WebViewActivity.this);
                final Map<String, String> result = alipay.payV2(paramReq, true);
                Handler handlerThree = new Handler(Looper.getMainLooper());
                handlerThree.post(new Runnable() {
                    public void run() {
                        PayResult payResult = new PayResult((Map<String, String>) result);
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            ToastUtil.showTips(mContext, "支付宝支付成功！");
                        } else {
                            ToastUtil.showTips(mContext, "支付宝支付失败！");

                        }
                        if (payCallBackListening != null) {
                            payCallBackListening.payResult();
                        }

                    }
                });
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    //高德
    @Override
    public void openLocation(final CompletionHandler<String> completionHandler) {
        Handler handlerThree = new Handler(Looper.getMainLooper());
        handlerThree.post(new Runnable() {
            public void run() {
                initMapLoaction(completionHandler);
                startMapLocation();
            }
        });
    }
    private BottomDialog bottomDialog;
    //微信分享
    @Override
    public void WXShare(final String url, final String title, final String descroption, final String imgurl) {
        bottomDialog = BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        v.findViewById(R.id.lin_weixin).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                share(0,url, title, descroption, imgurl);
                                bottomDialog.dismiss();
                            }
                        });
                        v.findViewById(R.id.lin_friend).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                share(1,url, title, descroption, imgurl);
                                bottomDialog.dismiss();
                            }
                        });

                    }
                })
                .setLayoutRes(R.layout.share_dialog_layout)
                .setDimAmount(0.4f)
                .setTag("BottomDialog");
        bottomDialog.show();

        wxShare.setListener(new WXShare.OnResponseListener() {
            @Override
            public void onSuccess() {
                // 分享成功
                Log.e("abc", "1");
                ToastUtil.showTips(WebViewActivity.this, "分享成功");
            }

            @Override
            public void onCancel() {
                // 分享取消
                Log.e("abc", "2");
                ToastUtil.showTips(WebViewActivity.this, "取消分享");
            }

            @Override
            public void onFail(String message) {
                // 分享失败
                Log.e("abc", "0");
            }
        });
    }

    private void share(final int type,final String url, final String title, final String descroption, final String imgurl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap thumb = BitmapFactory.decodeStream(new URL(imgurl).openStream());//注意下面的这句压缩，120，120是长宽。//一定要压缩，不然会分享失败
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(thumb, 120, 120, true);
                    thumb.recycle();

                    //1 朋友圈 0 朋友
                    wxShare.shareUrl(type, url, title, descroption, thumbBmp);
                    //            msg.setThumbImage(thumb);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}

