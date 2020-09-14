package com.chuyu.nfc.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chuyu.nfc.R;
import com.chuyu.nfc.base.BaseActivity;
import com.chuyu.nfc.tools.EventBus.EventCenter;
import com.chuyu.nfc.tools.StringUtil;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * 名称：二维码识别 创建人：ctw 创建时间：2017/4/10 16:30 详细说明：
 */
public class ScanCodeActivity extends BaseActivity implements DecoratedBarcodeView.TorchListener{


	private CaptureManager captureManager;
	private DecoratedBarcodeView mDBV;
	private ImageView mTop_bar_leftImg;

	private int layerId;
	private String call;

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.scan_code_activity;
	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	protected void getBundleExtras(Bundle extras) {
		if(extras==null) {
			return;
		}
/*		try{
			layerId=extras.getInt("layerId");
		}catch (Exception e){
			e.printStackTrace();
		}
		try{
			call=extras.getString("call");
		}catch (Exception e){
			e.printStackTrace();
		}*/
		try {
			String params=extras.getString(Intents.Scan.FORMATS);
			if(!StringUtil.isEmpty(params)){
				String[] paramArray=params.split(",");
				if(paramArray!=null && paramArray.length>0){
					layerId=Integer.parseInt(paramArray[0]);

				}
				if(paramArray!=null && paramArray.length>1){
					call=paramArray[1];
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDBV = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
		mDBV.setTorchListener(this);
		captureManager = new CaptureManager(this, mDBV);
		captureManager.initializeFromIntent(getIntent(),savedInstanceState);
		captureManager.decode();
    }

	@Override
	protected boolean isRegisterEventBusHere() {
		return false;
	}

	@Override
	protected void eventBusResult(EventCenter eventCenter) {

	}

	@Override
	protected void initView() {

		mTop_bar_leftImg = (ImageView) findViewById(R.id.top_bar_leftImg);

		mTop_bar_leftImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void initDatas() {


	}




	@Override
	protected void onPause() {
		super.onPause();
		captureManager.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		captureManager.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		captureManager.onDestroy();
	}

	@Override
	public void onTorchOn() {

	}

	@Override
	public void onTorchOff() {

	}

	@Override
	public void onClick(View v) {

	}
}
