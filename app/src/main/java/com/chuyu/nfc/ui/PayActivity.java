package com.chuyu.nfc.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.base.BaseAppManager;
import com.chuyu.nfc.base.URLs;
import com.chuyu.nfc.bean.Alipayinfo;
import com.chuyu.nfc.bean.OrderInfo;
import com.chuyu.nfc.bean.WXPayInfo;
import com.chuyu.nfc.bean.WXPayResult;
import com.chuyu.nfc.constant.Constants;
import com.chuyu.nfc.cusview.SweetAlert.SweetAlertDialog;
import com.chuyu.nfc.http.CallNet;
import com.chuyu.nfc.http.ConnectTask;
import com.chuyu.nfc.http.ParamUtil;
import com.chuyu.nfc.pay.wxpay.Sign;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.PayResult;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.tools.bar.SnackbarUtils;
import com.chuyu.nfc.webview.WebViewActivity;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;


public class PayActivity extends BaseActivity {

    private TextView typeTV;
    private LinearLayout echargeLayout;
    private ImageView back;
    private TextView topBarTitleTv;
    private TextView cost;
    private LinearLayout layout1;
    private CheckBox cb1;
    private LinearLayout layout2;
    private CheckBox cb2;
    private LinearLayout layout3;
    private CheckBox cb3;
    private CheckBox protocolCb;
    private TextView money;
    private Button pay;
    private EditText edittext;
    private TextView xieyi;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     9000 	订单支付成功
                     8000 	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                     4000 	订单支付失败
                     5000 	重复请求
                     6001 	用户中途取消
                     6002 	网络连接出错
                     6004 	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                     其它 	其它支付错误
                     */

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    Log.e("abc", resultStatus + resultInfo);
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        finish();
                        PayResultActivity.startAct(PayActivity.this, 1,orderid);
                        Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtil.showTips(PayActivity.this, payResult.getMemo() + "");
                    }
                    /**
                     支付返回的json数据
                     {
                     "alipay_trade_app_pay_response": {
                     "app_id": "2014100900013222",
                     "auth_app_id": "2014100900013222",
                     "charset": "UTF-8",
                     "code": "10000",
                     "msg": "Success",
                     "out_trade_no": "201811230229357802100",
                     "seller_id": "2088501624560335",
                     "timestamp": "2018-11-23 14:56:45",
                     "total_amount": "0.01",
                     "trade_no": "2018112322001476961010827114"
                     },
                     "sign": "dNNHvivhKCmI42BNNkhvKRyK/pIXqxwzwNpEfDkGkVy8mIlmU8115C7URf9D9iqD9105vPIZXt3zJ0QRHSc7Ewo9tgIKlv/E5rP+TX2+qNZMUdFg36JPuya4hRSx9VuEezCAJ3cp92VKLz2TlQcUNMVm6dabhQ8LED0b+gPAlgpTlLM+UWFDtMtltaqNSMyQlSq4GQ0vTPZwQMl6jLi94c+hhpGlsMSzKZOH62gmLaJPB1D9bXQbZFYWUBAigcwBVNM4TW8naN+5nY6N87OcS1PmXl1lnUCkxhGy4BX42ZrXDG7xtKd3bQco9qqE3ndk39Bc0/l4Q0wEDz1MzL/lSQ==",
                     "sign_type": "RSA2"
                     }*/
                    break;
                }

                default:
                    break;
            }
        }
    };
    private String orderid="";

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_pay;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        if (null!=extras.getString("orderid")){
            orderid = extras.getString("orderid");
        }
    }

    /**
     * @param paramAct tab标签
     */
    public static void startAct(Activity paramAct,String orderid) {
        Intent intent = new Intent(paramAct, PayActivity.class);
        intent.putExtra("orderid",orderid);
        paramAct.startActivity(intent);
    }
    /**
     * @param paramAct tab标签
     */
    public static void startAct(Activity paramAct) {
        Intent intent = new Intent(paramAct, PayActivity.class);
        paramAct.startActivity(intent);
    }

    @Override
    protected void initView() {
        back = (ImageView) findViewById(R.id.top_bar_leftImg);
        topBarTitleTv = (TextView) findViewById(R.id.top_bar_titleTv);
        typeTV = (TextView) findViewById(R.id.typeTV);//订单金额  or 余额
        echargeLayout = (LinearLayout) findViewById(R.id.echargeLayout);//充值布局
        edittext = (EditText) findViewById(R.id.edittext);
        cost = (TextView) findViewById(R.id.cost);//费用
        layout1 = (LinearLayout) findViewById(R.id.layout1);//微信支付
        cb1 = (CheckBox) findViewById(R.id.cb1);
        layout2 = (LinearLayout) findViewById(R.id.layout2);//支付宝支付
        cb2 = (CheckBox) findViewById(R.id.cb2);
        layout3 = (LinearLayout) findViewById(R.id.layout3);//第三方支付
        cb3 = (CheckBox) findViewById(R.id.cb3);
        protocolCb = (CheckBox) findViewById(R.id.protocol_cb);//协议
        xieyi = (TextView) findViewById(R.id.xieyi);
        money = (TextView) findViewById(R.id.money);//金额
        pay = (Button) findViewById(R.id.pay);//支付
        back.setOnClickListener(this);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        pay.setOnClickListener(this);
        xieyi.setOnClickListener(this);
        topBarTitleTv.setText("立即支付");
        if (orderid.isEmpty()){
            //充值
            typeTV.setText("余额");
            echargeLayout.setVisibility(View.VISIBLE);
        }else {
            //支付
            typeTV.setText("订单费用");
            echargeLayout.setVisibility(View.GONE);
        }
        edittext.addTextChangedListener(mTextWatcher);
    }
    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            money.setText(s+".00");//将输入的内容实时显示
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_leftImg:
                onBackPressed();
                break;
            case R.id.layout1://微信支付
                selection(1);
                break;
            case R.id.layout2://支付宝支付
                selection(2);
                break;
            case R.id.layout3://第三方支付
                SnackbarUtils.Short(layout3,"功能正在开发").show();
//                selection(3);
                break;
            case R.id.pay://支付
                if (protocolCb.isChecked()) {
                    if (payType == 1) {//微信
                        getPayinfo();
                    } else if (payType == 2) {//支付宝
                        alipay();
                    } else {
                        PayResultActivity.startAct(this, 1,orderid);
                    }
                } else {
                    ToastUtil.showTips(this, "请同意孝德天下服务协议");
                }
                break;
            case R.id.xieyi://支付协议
                WebViewActivity.startAct(this, null,
                        URLs.H5_URL + "/agreement/agreement.html?id=5");//支付协议
                break;

        }
    }

    private void getPayinfo() {
        Map<String, Object> map = ParamUtil.init();
        map.put("orderNo",orderid);
        CallNet.callNetNohttp(ParamUtil.createMy(URLs.WETPAY, map), new ConnectTask<WXPayInfo>(new TypeToken<WXPayInfo>() {
        }, this) {

            @Override
            public void openLoading() {
            }

            @Override
            public void onSuccess(WXPayInfo rsData, int eCode, String eMsg) {
                super.closeLoading();
                if (rsData != null) {
                    wxpay(rsData);
                }

            }

            @Override
            public void onFailure(int eCode, String eMsg) {
                closeLoading();
            }
        });
    }

    private IWXAPI api;
    private void wxpay(WXPayInfo wxPayInfo) {
        api = WXAPIFactory.createWXAPI(PayActivity.this, null);
        api.registerApp(Constants.WX_APP_ID);
        try {
            PayReq req = new PayReq();
            req.appId = Constants.WX_APP_ID;
            req.partnerId = wxPayInfo.getMch_id();
            req.prepayId = wxPayInfo.getPrepay_id();
            req.nonceStr = wxPayInfo.getNonce_str();
            req.timeStamp = wxPayInfo.getTime_str();
            req.packageValue = wxPayInfo.getPackages();
//            req.sign=wxPayInfo.getPaySign();//千万不能使用后台返回的签名，否则巨坑。请不要再踩坑了。
            req.sign = Sign.genPayReq(req);//一定要自己重新签名。
            req.extData = "";
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.showTips(PayActivity.this, "支付异常，支付失败！");
        }
    }

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2014100900013222";
    /**
     * 商户私钥
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC98aoOKirBjPTKN4vLTi8vpYwWTLG6WJh701BnCl3C5gT3lQQGeXf+ZphXBwD9e3fRyJai9cpxiis7eUbonrqrmUoIvbyiAudoYpqnXVKTcaO/rSisr9aa7IOfMqFHEOS61nIlceJja2txCJfUNanS89aghMcvqHLcJ5m0YKN4cimbUUYPELMVI1PsCJyJUNKFiYorQq1/TWWqbvaKRaDRk8NHJgHsKtZFlyhbnm73NsP69NOdKvjQvTlLfpFZE+l8I61sy8Cz4V2E67bwJ2r9/G4f0FVt+F/U4YVegax4w8+w5Y55veazDJrm2gJ4abNk0EETZwyBLPgsbFR2/y3hAgMBAAECggEAe9iVQ7UUuaxZc3wyJvYsaAmtxGBvRYw8qAgJFZY5ujlWJcPAoyQSLAri62OCrsQRRPRf25MdU1h+hcG2jTfpiLdjAT4NPylbjsE0C0oa7E4dMX4K1kW0TMFHtMZDR93o9TWbqXSO4roIjOPIczImL4iTeYf5g8Z2VbtwSZ71FzNgmuAKLy7eobLfewXo1daqBe0ZDGk75RkQ2NR6cJmbLLMbBlTqAbQyBA8KIdue3gR5rruecgsdoMEEBnBPTrZ07U26l1+m3k2lHjE3EsztsQBMDpUwdFKuq4kk7gRBXolj8zqYoKRiOVWy88bvPz8Vx8Vo6XGa0X0S/yBnM5t3kQKBgQDopuLxmxS/3MWcJsIKRYLKUlnWe/So0HbFsV6ij/So/8rfcWkzh9k+qnnSBHtvkQFsnv0+GLd1Hy8Wad9vCyFDessvhuzQRY2Imk9FotaTkYbrchrz0reJUYY6nUklyCTxEAz2UxyLLEIQO0JAhXH8S18YNuh6hVEphmKT2NxpbQKBgQDRAY8vGqJzvznIcCdXXoJx4Ic65dnPRwoEB1abQHnpYqmf8VH/Styk7By1Ap+luP7X3T6QX4gSR4RqC5krJBoz9nAsucgo+t9aUXrhLV83hOoKWD7znX/C1+0b/osRPoexlBmaaf+n+RNIQ3hxU9uIl6WHT01daBL4GVBanxchxQKBgES/e/R1JS6E6If6FADBBaMPrqhovKVd5JsKjLJw45VE8QgSFUo67IFOEu1ykZ8oNEmKub6twxiC/IEdC/9eRJgSIxSKRFRPGUGyh5ZGRi4ZJMtSTpCaRc34HzgW3lShzfjGC26GpLqje2oceLlkNYieJR2crBn4Z0FkCqExxgAJAoGBAMdW/2NjudE/bzMWlM8lmrBV/2RTWPvyu0DAZv/H7P6FVVbw6M3ebrb1YyPZDr8WxCjKISO9maAlictCqKGW2074GmDuCFPdgi04TUR667eeE0IujEv5yaLiIolyqtyVkQHzSMAXnPht/NANWdBstJOAXyXAov8VhhIOwq7L0VopAoGAeZBY2TXgCvvD/7sDQ8hQjxEI0IVORT35bGoNtSce2QDa9lG5cwXOn775TcAhCiWAmLexRKo+1Q/aYVAYTTB6ImEvE5DVb+3vz7a+1Wgw8yz+pijwabVRT0oggBYRWFyNRVjIrrwg3Tg5Me+P65/p64ztOhDotbQl6mtF0CshC44=";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;

    private void alipay() {

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        Map<String, Object> map = ParamUtil.init();
        map.put("orderNo",orderid);
        CallNet.callNetNohttp(ParamUtil.createMy(URLs.ALIPAY, map), new ConnectTask<Alipayinfo>(new TypeToken<Alipayinfo>() {
        }, this) {

            @Override
            public void openLoading() {
            }

            @Override
            public void onSuccess(Alipayinfo rsData, int eCode, String eMsg) {
                super.closeLoading();
                if (rsData != null) {
                    final String orderInfo = rsData.getOrderInfo();
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            // 构造PayTask 对象
                            PayTask alipay = new PayTask(PayActivity.this);
                            // 调用支付接口，获取支付结果
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }
            @Override
            public void onFailure(int eCode, String eMsg) {
                closeLoading();
            }
        });


    }


    private int payType = 1;

    private void selection(int i) {
        payType = i;
        if (i == 1) {
            cb1.setChecked(true);
            cb2.setChecked(false);
            cb3.setChecked(false);
        } else if (i == 2) {
            cb1.setChecked(false);
            cb2.setChecked(true);
            cb3.setChecked(false);
        } else if (i == 3) {
            cb1.setChecked(false);
            cb2.setChecked(false);
            cb3.setChecked(true);
        }
    }

    @Override
    protected void initDatas() {
        BaseAppManager.getInstance().clear(1);//关闭上一页
        if (!orderid.isEmpty()){
            //通过订单号请求订单信息
            Map<String, Object> map = ParamUtil.init();
            map.put("order_no",orderid);
            CallNet.callNetNohttp(ParamUtil.createMy(URLs.ORDERAMOUNT, map), new ConnectTask<OrderInfo>(new TypeToken<OrderInfo>() {
            }, this) {

                @Override
                public void openLoading() {
                }

                @Override
                public void onSuccess(OrderInfo rsData, int eCode, String eMsg) {
                    super.closeLoading();
                    if (rsData != null) {
                        cost.setText(rsData.getAmount());
                        money.setText(rsData.getAmount());
                    }
                }
                @Override
                public void onFailure(int eCode, String eMsg) {
                    closeLoading();
                }
            });
        }
    }


    @Override
    protected boolean isRegisterEventBusHere() {
        return true;
    }

    @Override
    protected void eventBusResult(EventCenter eventCenter) {
        switch (eventCenter.getEventCode()) {
            case EventCode.CODE31:
                WXPayResult payResult = (WXPayResult) eventCenter.getData();
                if (payResult != null && payResult.getCode() == 1) {
                    //支付成功 关掉这个页面 跳到支持成功
                    finish();
                    PayResultActivity.startAct(this, 1,orderid);
                }else if (payResult != null && payResult.getCode() == -1){
                    //微信支付失败
                }else if (payResult != null && payResult.getCode() == -2){
                    //已取消支付
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {

        final SweetAlertDialog sd = new SweetAlertDialog(this);
        sd.setCancelable(true);
        sd.setCanceledOnTouchOutside(true);
        sd.setTitleText("退出支付?");
        sd.setConfirmText("确定");
        sd.showCancelButton(true);
        sd.setCancelText("取消");
        sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                finish();
            }
        });
        sd.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                final SweetAlertDialog sd = new SweetAlertDialog(this);
                sd.setCancelable(true);
                sd.setCanceledOnTouchOutside(true);
                sd.setTitleText("退出支付?");
                sd.setConfirmText("确定");
                sd.showCancelButton(true);
                sd.setCancelText("取消");
                sd.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                      finish();
                    }
                });
                sd.show();
            }
            return true;
        }
        return false;
    }

}

