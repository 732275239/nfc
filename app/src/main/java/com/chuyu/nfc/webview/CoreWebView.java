package com.chuyu.nfc.webview;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuyu.nfc.listening.CoreWebViewListening;
import com.chuyu.nfc.tools.NetUtil;
import com.chuyu.nfc.tools.StringUtil;

import wendu.dsbridge.DWebView;

import static com.chuyu.nfc.base.Constants.LOG_TAG;

/**
 * Created by Administrator on 2018/9/27 0027.
 */

public class CoreWebView extends DWebView{


    private Activity mContxt;
    private String loadingUrl = "";
    private CoreWebViewListening coreWebViewListening;
    private Handler handler;
    private CoreWebParamBean coreWebParamBean;

    public static CoreWebView getInstance(Activity context, Handler handler, CoreWebViewListening coreWebViewListening, CoreWebParamBean paramBean){
        return new CoreWebView(context,handler,coreWebViewListening,paramBean);
    }

    public CoreWebView(Activity context,Handler handler,CoreWebViewListening coreWebViewListening,CoreWebParamBean paramBean){
        super(context);
        this.mContxt=context;
        this.handler=handler;
        this.coreWebViewListening=coreWebViewListening;
        if(paramBean==null){
            coreWebParamBean=new CoreWebParamBean();
        }else{
            coreWebParamBean=paramBean;
        }
        initEvent();
    }

    public void initData(String loadingUrl){
        try{
            this.loadingUrl=loadingUrl;
            if(!TextUtils.isEmpty(loadingUrl)){
                if(loadingUrl.startsWith("file:///")){
                    this.loadUrl(loadingUrl);
                }else{
                    if(NetUtil.isNetwork(mContxt)){
                        this.loadUrl(loadingUrl);
                    }else{
                        delError(null);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void loadData(){
        try{
            if(!TextUtils.isEmpty(loadingUrl)){
                this.loadUrl(loadingUrl);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void openProgress() {
        try{
            if(coreWebViewListening!=null){
                coreWebViewListening.openBgLoading("");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void closeProgress() {
        try{
            if(coreWebViewListening!=null && coreWebParamBean.isHasLoading()){
                coreWebViewListening.closeBgLoading();
            }
            //if(showErrorStatus==false) {

            //}
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(LOG_TAG,"请求url===="+url);
            if(TextUtils.isEmpty(url)){
                return false;
            }
            if(url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file:///") || url.startsWith("tel:")){
                view.loadUrl(url);
                return true;
            }else{
                return false;
            }

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            closeProgress();
            delError(view);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            openProgress();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            closeProgress();
            super.onPageFinished(view, url);
            view.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        /**
         * 可对HTML中的按钮事件进行响应
         */
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

    };

    WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if(!StringUtil.isEmpty(title)){
                if("网页无法打开".equals(title) || title.toLowerCase().contains("error") || "找不到网页".equals(title) || "Runtime Error".equals(title)){
                    delError(view);
                }else{
                    if (coreWebViewListening != null) {
                        if(title.startsWith("http://") || title.startsWith("https://")){
                            title="";
                        }
                        coreWebViewListening.webTitle(title);
                    }
                }
            }
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (onProgress!=null){
                onProgress.onProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
            if (newProgress >= 100) {
                view.getSettings().setBlockNetworkImage(false);
            }
        }


    };

    private void delError(WebView view){
        if (coreWebViewListening != null) {
            coreWebViewListening.showError();
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void initEvent() {
        this.setWebViewClient(mWebViewClient);
        this.setWebChromeClient(mWebChromeClient);
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDefaultTextEncodingName("utf-8");
        this.getSettings().setSupportZoom(true);
        this.getSettings().setBuiltInZoomControls(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setDisplayZoomControls(false);
        //this.addJavascriptInterface(new CoreWebJavaScript(mContxt,handler,this,null), CoreWebJavaScript.WEB_JS_KEY);
        this.addJavascriptObject(new CoreWebJavaScript(mContxt,handler,coreWebViewListening,null), CoreWebJavaScript.WEB_JS_KEY);
        this.setWebContentsDebuggingEnabled(true);
        //this.addJavascriptObject(new CoreWebJavaScript(mContxt,handler,coreWebViewListening,null), null);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings webSetting = this.getSettings();
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSetting.setDefaultTextEncodingName("UTF-8");
        webSetting.setDomStorageEnabled(true);

        webSetting.setBlockNetworkImage(true);//设置网页在加载的时候暂时不加载图片

        webSetting.setSaveFormData(false);
        // 设置可以支持缩放
        webSetting.setSupportZoom(true);
        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSetting.setUseWideViewPort(true);
        // 设置默认加载的可视范围是大视野范围
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);// 打开缓存，如果没有网络
        webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
        String uaStr = webSetting.getUserAgentString();

        // 启用数据库
        webSetting.setDatabaseEnabled(true);
        //String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        // 启用地理定位
        webSetting.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        //webSetting.setGeolocationDatabasePath(dir);

        // 最重要的方法，一定要设置，这就是出不来的主要原因
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);// 设置支持javascript脚本
        webSetting.setBuiltInZoomControls(true);// 设置支持缩放

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        this.requestFocus(); // 输入框点击弹出软键盘
    }


    public void executeJavaScript(String jsFunction){
        try{
            if(TextUtils.isEmpty(jsFunction)){
                return;
            }
            if(!jsFunction.contains("(")){
                jsFunction=jsFunction+"(";
            }
            if(!jsFunction.contains(")")){
                jsFunction=jsFunction+")";
            }
            if (Build.VERSION.SDK_INT < 19) {
                this.loadUrl("javascript:"+jsFunction);
            } else {
                this.evaluateJavascript("javascript:"+jsFunction, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public interface onProgress{
        void onProgress(int progress);
    }
    private onProgress onProgress;

    public void setOnProgress(onProgress onProgress){
        this.onProgress=onProgress;
    }
}
