package com.chuyu.nfc.webview;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.ApplicationContext;
import com.chuyu.nfc.base.Constants;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.WebHandleBean;
import com.chuyu.nfc.listening.ConfirmDialogOkListening;
import com.chuyu.nfc.listening.CoreWebViewListening;
import com.chuyu.nfc.listening.PayCallBackListening;
import com.chuyu.nfc.listening.UpRoundHandleListening;
import com.chuyu.nfc.listening.UploadImgHandleListening;
import com.chuyu.nfc.tools.AlertUtils;
import com.chuyu.nfc.tools.JsonUtil;
import com.chuyu.nfc.tools.NetUtil;
import com.chuyu.nfc.tools.share.SharedPreferencesUtils;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.ui.ForgotPasswordActivity;
import com.chuyu.nfc.ui.LoginActivity;
import com.chuyu.nfc.ui.MainActivity;
import com.chuyu.nfc.ui.PayActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.OptionPicker;
import wendu.dsbridge.CompletionHandler;


public class CoreWebJavaScript {

    public static final String WEB_JS_KEY = "qr";
    private Activity mContxt;
    private View titleBarView;
    private Handler handler;
    private CoreWebViewListening webEvent;

    public CoreWebJavaScript(Activity mContxt, Handler handler, CoreWebViewListening coreWebViewListening, View titleBarView) {
        this.mContxt = mContxt;
        this.handler = handler;
        this.webEvent = coreWebViewListening;
        this.titleBarView = titleBarView;

    }

    @JavascriptInterface
    public void setParams(Object paramObj, final CompletionHandler<String> handler) {
        try {
            if (paramObj == null) {
                return;
            }
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            String key = param.get("key");
            String parmas = param.get("parmas");
            SharedPreferencesUtils.setParam(mContxt, key, parmas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public String getParams(Object paramObj) {
        try {
            if (paramObj == null) {
                return "";
            }
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            String key = param.get("key");
            return (String) SharedPreferencesUtils.getParam(mContxt, key, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @JavascriptInterface
    public void clearParams(Object paramObj, final CompletionHandler<String> handler) {
        try {
            if (paramObj == null) {
                return;
            }
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            String key = param.get("key");
            SharedPreferencesUtils.clearParam(mContxt, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //高德
    @JavascriptInterface
    public void openLocation(Object paramObj, final CompletionHandler<String> handler) {
        try {
            webEvent.openLocation(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //微信分享
    @JavascriptInterface
    public void wxShare(Object paramObj, final CompletionHandler<String> handler) {
        try {
            if (paramObj == null) {
                return;
            }
//            String url,String title,String descroption,String imgurl
            Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
            String url = (String) param.get("url");
            String title = (String) param.get("title");
            String descroption = (String) param.get("descroption");
            String imgurl = (String) param.get("imgurl");
            webEvent.WXShare(url, title, descroption, imgurl);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开新页面 关闭上几级页面、
    @JavascriptInterface
    public void filshOpenWin(Object paramObj, CompletionHandler<String> handler) {

        try {
            if (paramObj == null) {
                ToastUtil.showTips(mContxt, "跳转链接无效！");
                return;
            }
            Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
            String url = (String) param.get("url");
            Object titleObj = param.get("title");
            Object loadObj = param.get("load");
            String filshSize = (String) param.get("size");
            String title = "";
            boolean hasLoad = true;
            try {
                if (titleObj != null) {
                    title = titleObj.toString();
                }
                if (loadObj != null) {
                    hasLoad = Boolean.parseBoolean(loadObj.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(url)) {
                ToastUtil.showTips(mContxt, "跳转链接无效！");
                return;
            }
            if (title == null) {
                title = "";
            }
//            if (url.startsWith("/")) {
//                //url = "file:///android_asset/web" + url;
//                url = URLs.H5_URL + url;
//            }
            int i = Integer.parseInt(filshSize);
            webEvent.fishOpenWin(url, title, hasLoad, i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void openWin(Object paramObj, CompletionHandler<String> handler) {
        try {
            if (paramObj == null) {
                ToastUtil.showTips(mContxt, "跳转链接无效！");
                return;
            }
            Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
            String url = (String) param.get("url");
            Object titleObj = param.get("title");
            Object loadObj = param.get("load");
            String title = "";
            boolean hasLoad = true;
            try {
                if (titleObj != null) {
                    title = titleObj.toString();
                }
                if (loadObj != null) {
                    hasLoad = Boolean.parseBoolean(loadObj.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(url)) {
                ToastUtil.showTips(mContxt, "跳转链接无效！");
                return;
            }
            if (title == null) {
                title = "";
            }
//            if (url.startsWith("/")) {
//                //url = "file:///android_asset/web" + url;
//                url = URLs.H5_URL + url;
//            }
            webEvent.openWin(url, title, hasLoad);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //是否登录
    @JavascriptInterface
    public boolean isLogin(Object paramObj) {
        try {
            return ApplicationContext.getInstance().getSpTools().ifLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    //退出登录
    @JavascriptInterface
    public void logOut(Object paramObj, CompletionHandler<String> handler) {
        try {
            ApplicationContext.getInstance().getSpTools().clearUser();
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setTag(0);
            sendPost(Constants.EVENT_16, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //刷新个人中心的数据
    @JavascriptInterface
    public void refreshHomepage(Object paramObj, CompletionHandler<String> handler) {
        try {
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setTag(0);
            sendPost(Constants.EVENT_17, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //在当前页面加载一个url new
    @JavascriptInterface
    public void loadUrl(Object paramObj) {
        if (paramObj == null) {
            ToastUtil.showTips(mContxt, "跳转链接无效！");
            return;
        }
        Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
        String url = (String) param.get("url");
//        if (url.startsWith("/")) {
//            //url = "file:///android_asset/web" + url;
//            url = URLs.H5_URL + url;
//        }
        webEvent.loadUrl(url);
    }

    //刷新当前页面
    @JavascriptInterface
    public void refreshWin(Object paramObj) {
        webEvent.refreshWin();
    }

    @JavascriptInterface
    public void openLoginWin(Object paramObj, CompletionHandler<String> handler) {
        try {
            boolean isRefresh = false;
            if (paramObj == null) {
                isRefresh = false;
            } else {
                try {
                    Map<String, Boolean> param = JsonUtil.jsonToMap(paramObj.toString());
                    Boolean refresh = param.get("isRefresh");
                    isRefresh = refresh.booleanValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            LoginActivity.startAct(mContxt, isRefresh);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getToken(Object o) {
        try {
            return ApplicationContext.getInstance().getSpTools().getLogin().getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @JavascriptInterface
    public void closeWin(Object paramObj, CompletionHandler<String> handler) {
        try {
            if (paramObj != null) {
                Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
                if (param != null && !param.isEmpty()) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("callFunction", param.get("callFn"));
                    intent.putExtras(bundle);
                    mContxt.setResult(Constants.RESPONSE_CODE_1002, intent);
                }
            }
            mContxt.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void closeWinToast(Object paramObj, CompletionHandler<String> handler) {
        try {
            if (paramObj != null) {
                Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
                if (param != null && !param.isEmpty()) {
                    String toast = param.get("toast");
                    ToastUtil.showTips(mContxt, toast);
                }
            }
            mContxt.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //测试支付
    @JavascriptInterface
    public void goPay(Object paramObj) {
        try {
            if (paramObj != null) {
                Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
                String orderid = (String) param.get("orderid");
                PayActivity.startAct(mContxt, orderid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //测试充值
    @JavascriptInterface
    public void goRecharge(Object paramObj, CompletionHandler<String> handler) {
        try {
            PayActivity.startAct(mContxt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //重置密码
    @JavascriptInterface
    public void forgotPassword(Object paramObj, CompletionHandler<String> handler) {
        try {
            mContxt.startActivity(new Intent(mContxt, ForgotPasswordActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //拨打电话
    @JavascriptInterface
    public void CallPhone(Object paramObj, CompletionHandler<String> handler) {
        if (paramObj == null) {
            ToastUtil.showTips(mContxt, "号码错误");
            return;
        }
        try {
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            String phone = param.get("phone");
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContxt.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void openWeChatPay(Object paramObj, final CompletionHandler<String> handler) {
        try {

            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            if (param == null) {
                return;
            }
            PayReq req = new PayReq();
            req.appId = param.get("appid");
            req.partnerId = param.get("partnerid");
            req.prepayId = param.get("prepayid");
            req.nonceStr = param.get("noncestr");
            req.timeStamp = param.get("timestamp");
            req.packageValue = param.get("package");
            req.sign = param.get("sign");//一定要自己重新签名。
            //req.sign = Sign.genPayReq(req);//一定要自己重新签名。
            req.extData = ""; // optional

            if (titleBarView != null) {
                titleBarView.setTag("99999APAY");
            }

            webEvent.openWeChatPay(req, new PayCallBackListening() {
                @Override
                public void payResult() {
                    handler.complete("");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @JavascriptInterface
    public void openAlipay(Object paramObj, final CompletionHandler<String> handler) {
        try {
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            if (param == null) {
                return;
            }
            String data = param.get("payParam");
            if (TextUtils.isEmpty(data)) {
                return;
            }

            webEvent.openAlipay(data, new PayCallBackListening() {
                @Override
                public void payResult() {
                    handler.complete("");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void tips(Object paramObj, CompletionHandler<String> handler) {
        try {
            String msg = "";
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            msg = param.get("msg");
            if (!TextUtils.isEmpty(msg)) {
                ToastUtil.showTips(mContxt, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void showRightTextBtn(Object paramObj, final CompletionHandler<String> handler) {
        try {
            String title = "";
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            title = param.get("title");
            webEvent.showRightTextBtn(title, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.setProgressData("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void showRightImgBtn(Object paramObj, final CompletionHandler<String> handler) {
        try {
            String imgUrl = "";
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            if (param == null) {
                return;
            }

            imgUrl = param.get("imgUrl");

            if (TextUtils.isEmpty(imgUrl)) {
                return;
            }
            if (imgUrl.startsWith("/")) {
                imgUrl = URLs.H5_URL + imgUrl;
            }
            webEvent.showRightImgBtn(imgUrl, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handler.setProgressData("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void openOptionPicker(Object paramObj, final CompletionHandler<Integer> handler) {
        try {
            String[] array = new String[0];
            int selectedIndex = 0;

            if (paramObj != null) {
                Map<String, Object> param = JsonUtil.jsonToMap(paramObj.toString());
                if (param != null) {
                    Double d = (Double) param.get("default");
                    selectedIndex = (int) d.doubleValue();
                    Object listObj = param.get("option");
                    ArrayList<String> lists = null;
                    if (listObj != null) {
                        lists = (ArrayList<String>) listObj;
                    }
                    if (lists != null && lists.size() > 0) {
                        array = new String[lists.size()];
                        lists.toArray(array);
                    }

                }
            }

            if (array.length <= 0) {
                ToastUtil.showTips(mContxt, "暂无数据");
                return;
            }


            final String[] newArray = array;
            final int newSelectedIndex = selectedIndex;
            Handler handlerThree = new Handler(Looper.getMainLooper());
            handlerThree.post(new Runnable() {
                public void run() {

                    OptionPicker picker = new OptionPicker(mContxt, newArray);
                    //picker.setOffset(2);
                    picker.setSelectedIndex(newSelectedIndex);
                    int themeColor = ContextCompat.getColor(mContxt, R.color.app_theme);
                    picker.setLineColor(themeColor);
                    picker.setTopLineColor(themeColor);
                    picker.setCancelTextColor(themeColor);
                    picker.setLabelTextColor(themeColor);
                    picker.setDividerVisible(false);
                    picker.setSubmitTextColor(themeColor);
                    picker.setCancelTextSize(18);
                    picker.setSubmitTextSize(18);
                    picker.setTextSize(20);
                    picker.setTextColor(themeColor);
                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int index, String item) {
                            if (handler != null) {
                                handler.complete(index);
                            }
                        }
                    });
                    picker.setAnimationStyle(R.style.Animation_CustomPopup);
                    picker.show();


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void openLoading(Object paramObj, CompletionHandler<String> handler) {
        try {
            webEvent.openLoading("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void openBgLoading(Object paramObj, CompletionHandler<String> handler) {
        try {
            webEvent.openBgLoading("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void gohome(Object paramObj, CompletionHandler<String> handler) {
        try {
            Intent intent = new Intent(mContxt, MainActivity.class);
            mContxt.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void closeLoading(Object paramObj, CompletionHandler<String> handler) {
        try {
            webEvent.closeLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void closeBgLoading(Object paramObj, CompletionHandler<String> handler) {
        try {
            webEvent.closeBgLoading();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void alert(Object paramObj, final CompletionHandler<Integer> handler) {
        try {
            Map<String, String> param = JsonUtil.jsonToMap(paramObj.toString());
            if (param == null) {
                return;
            }
            Object titleObj = param.get("title");
            Object msgObj = param.get("message");

            String title = "";
            String message = "";

            if (titleObj != null) {
                title = titleObj.toString();
            }

            if (msgObj != null) {
                message = msgObj.toString();
            }
            Handler handlerThree = new Handler(Looper.getMainLooper());
            final String finalTitle = title;
            final String finalMessage = message;
            handlerThree.post(new Runnable() {
                public void run() {
                    AlertUtils.confirmDialog(mContxt, finalTitle, finalMessage, new ConfirmDialogOkListening() {
                        @Override
                        public void clickDialogOk() {
                            if (handler != null) {
                                handler.complete(0);
                            }

                        }
                    });
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传图片
    @JavascriptInterface
    public void openUploadImg(Object paramObj, final CompletionHandler<String> handler) {
        try {
            webEvent.openUploadImg(new UploadImgHandleListening() {
                @Override
                public void uploadImgHandel(String imgPath, String imgUrl) {
                    Map<String, String> imgMap = new HashMap<String, String>();
                    imgMap.put("imgPath", imgPath);
                    imgMap.put("imgUrl", imgUrl);
                    String imgJson = JsonUtil.mapToJson(imgMap);
//                    handler.setProgressData("");//一直触发
                    handler.complete(imgJson);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传圆形图片
    @JavascriptInterface
    public void openRoundImg(Object paramObj, final CompletionHandler<String> handler) {
        try {
            webEvent.openRoundImg(new UpRoundHandleListening() {
                @Override
                public void upRoundHandel(String imgPath, String imgUrl) {
                    Map<String, String> imgMap = new HashMap<String, String>();
                    imgMap.put("imgPath", imgPath);
                    imgMap.put("imgUrl", imgUrl);
                    String imgJson = JsonUtil.mapToJson(imgMap);
                    handler.complete(imgJson);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void hideRightBtn() {
        try {
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setTag(0);
            sendPost(Constants.EVENT_04, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void hideLeftBtn() {
        try {
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setTag(1);
            sendPost(Constants.EVENT_04, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void showLeftBtn() {
        try {
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setTag(2);
            sendPost(Constants.EVENT_04, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void showNavgBar() {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setTag(3);
        sendPost(Constants.EVENT_04, webHandleBean);
    }

    @JavascriptInterface
    public void hideNavgBar() {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setTag(4);
        sendPost(Constants.EVENT_04, webHandleBean);
    }

    @JavascriptInterface
    public void showNavgBarBgColor(String color) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setParam(color);
        webHandleBean.setTag(5);
        sendPost(Constants.EVENT_04, webHandleBean);
    }

    @JavascriptInterface
    public void openScan(String call) {
        try {
            WebHandleBean webHandleBean = new WebHandleBean();
            webHandleBean.setCall(call);
            sendPost(Constants.EVENT_07, webHandleBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void alert(String title, String message) {
        try {
            AlertUtils.alert(mContxt, title, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @JavascriptInterface
    public void alertLoading(String message) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setParam(message);
        webHandleBean.setTag(2);
        sendPost(Constants.EVENT_05, webHandleBean);
    }


    @JavascriptInterface
    public void openPullLoaidng(String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setTag(0);
        webHandleBean.setCall(call);
        sendPost(Constants.EVENT_09, webHandleBean);
    }

    @JavascriptInterface
    public void openUpLoading(String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setTag(1);
        webHandleBean.setCall(call);
        sendPost(Constants.EVENT_09, webHandleBean);
    }


    @JavascriptInterface
    public boolean isNetWork() {
        try {
            return NetUtil.isNetwork(mContxt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @JavascriptInterface
    public void speak(String message) {
        try {
            if (TextUtils.isEmpty(message)) {
                return;
            }
            //TTSUtils.getInstance().speak(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public String getHostUrl() {
        try {
            return URLs.HOST_URL;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 日期时间选择器
     *
     * @param dataMode Y-M-D=年月日
     * @param call
     */
    @JavascriptInterface
    public void openDataPicker(String dataMode, String call) {
        if (TextUtils.isEmpty(dataMode)) {
            return;
        }
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setParam(dataMode);
        webHandleBean.setCall(call);
        sendPost(Constants.EVENT_10, webHandleBean);

    }


    @JavascriptInterface
    public void openAreaPicker(String dataMode, String[] areaArray, String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setCall(call);
        webHandleBean.setParam(dataMode);
        webHandleBean.setParams(areaArray);
        sendPost(Constants.EVENT_12, webHandleBean);
    }

    @JavascriptInterface
    public void openFilePicker(String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setCall(call);
        webHandleBean.setTag(0);
        sendPost(Constants.EVENT_13, webHandleBean);
    }

    @JavascriptInterface
    public void openDirPicker(String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setCall(call);
        webHandleBean.setTag(1);
        sendPost(Constants.EVENT_13, webHandleBean);
    }

    @JavascriptInterface
    public void openActionSheet(String[] items, String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setCall(call);
        webHandleBean.setParams(items);
        sendPost(Constants.EVENT_14, webHandleBean);
    }

    protected void sendPost(int event, String param) {
        sendPost(event, param, null);
    }

    protected void sendPost(int event, String param, String call) {
        WebHandleBean webHandleBean = new WebHandleBean();
        webHandleBean.setParam(param);
        webHandleBean.setCall(call);
        sendPost(event, webHandleBean);
    }

    protected void sendPost(int event, WebHandleBean webHandleBean) {
        try {
            if (handler == null) {
                return;
            }
            Message message = new Message();
            message.what = event;
            message.obj = webHandleBean;
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
