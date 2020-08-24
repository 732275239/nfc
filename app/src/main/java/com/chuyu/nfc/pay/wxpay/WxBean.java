package com.chuyu.nfc.pay.wxpay;

import java.io.Serializable;

public class WxBean implements Serializable {

	String appid;// 微信开放平台审核通过的应用APPID
	String partnerid;// 微信支付分配的商户号
	String prepayid;// 微信返回的支付交易会话ID
	String packages;// 扩展字段(此处多加了一个s)
	String sign;// 签名，详见签名生成算法
	String timestamp;// 时间戳，请见接口规则-参数规定
	String noncestr;// 随机字符串，不长于32位。推荐随机数生成算法

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getPackages() {
		return packages;
	}

	public void setPackages(String packages) {
		this.packages = packages;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

}
