package com.chuyu.nfc.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.chuyu.nfc.R;
import com.chuyu.nfc.constant.Constants;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.EventBus.EventCode;
import com.chuyu.nfc.tools.StringUtil;
import com.chuyu.nfc.tools.ToastUtil;
import com.chuyu.nfc.bean.WXPayResult;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("WXPAY", "微信支付" + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			int code = resp.errCode;
			switch (code) {
				case 0:
					ToastUtil.showTips(this, "微信支付成功！");
					WXPayResult payResult=new WXPayResult();
					payResult.setCode(1);
					payResult.setMsg("微信支付成功！");
					EventBus.getDefault().post(new EventCenter<>(EventCode.CODE31,payResult));
					finish();
					break;
				case -1:
					if(StringUtil.isEmpty(resp.errStr)){
						ToastUtil.showTips(this, "微信支付失败！");
					}else{
						ToastUtil.showTips(this, "支付失败,"+resp.errStr);
					}
					WXPayResult payResult1=new WXPayResult();
					payResult1.setCode(-1);
					payResult1.setMsg("微信支付失败！");
					EventBus.getDefault().post(new EventCenter<>(EventCode.CODE31,payResult1));
					finish();
					break;
				case -2:
					ToastUtil.showTips(this, "已取消支付！");
					WXPayResult payResult2=new WXPayResult();
					payResult2.setCode(-2);
					payResult2.setMsg("已取消支付！");
					EventBus.getDefault().post(new EventCenter<>(EventCode.CODE31,payResult2));
					finish();
					break;
				default:
					ToastUtil.showTips(this, "未知错误,微信支付失败！");
					finish();
					break;
			}
		}
	}
}