package com.chuyu.nfc.pay.wxpay;

import com.chuyu.nfc.constant.Constants;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 获取微信支付Sign
 * 
 * @author Administrator
 * 
 */
public class Sign {

	public static String genPayReq(PayReq req) {

		List<Map<String,String>> signParams = new LinkedList<Map<String,String>>();

		Map<String,String> p1=new HashMap<String,String>();
		p1.put("appid",req.appId);

		Map<String,String> p2=new HashMap<String,String>();
		p2.put("noncestr",req.nonceStr);

		Map<String,String> p3=new HashMap<String,String>();
		p3.put("package",req.packageValue);

		Map<String,String> p4=new HashMap<String,String>();
		p4.put("partnerid",req.partnerId);

		Map<String,String> p5=new HashMap<String,String>();
		p5.put("prepayid",req.prepayId);

		Map<String,String> p6=new HashMap<String,String>();
		p6.put("timestamp",req.timeStamp);

		signParams.add(p1);
		signParams.add(p2);
		signParams.add(p3);
		signParams.add(p4);
		signParams.add(p5);
		signParams.add(p6);

		// req.sign = genAppSign(signParams);
		return genAppSign(signParams);

	}

	private static String genAppSign(List<Map<String,String>> params) {
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			Map<String,String> kyy=params.get(i);
			String name="";
			String value="";
			for (Map.Entry<String, String> entry : kyy.entrySet()) {
				name=entry.getKey();
				value=entry.getValue();
				break;
			}
			stringBuilder.append(name);
			stringBuilder.append('=');
			stringBuilder.append(value);
			stringBuilder.append('&');
		}
		stringBuilder.append("key=");
		stringBuilder.append(Constants.WX_API_KEY);

		String appSign = MD5.getMessageDigest(
				stringBuilder.toString().getBytes()).toUpperCase();
		return appSign;
	}
}
