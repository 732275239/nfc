package com.chuyu.nfc.bean;

import java.io.Serializable;

/**
 * Created by xxu on 2018/5/14.
 */
public class WXPayInfo implements Serializable{
    /**
     * appId : wxfbdfa7d65cda8150
     * order_num : 201812060530501881918
     * device_num : 1
     * mch_id : 1519019411
     * prepay_id : wx061730496632713c000c1d673839288006
     * nonce_str : 4qoZdAXt8IusLM65
     * time_str : 1544088650
     * time_sys_str : null
     * packages : Sign=WXPay
     * paySign : BBA57B8C7FA00126FB787640726ABB7E
     * payMoney : 0.01
     * goWchatPay : 1
     * payStatus : 0
     * errorMsg : null
     */

    private String appId;
    private String order_num;
    private String device_num;
    private String mch_id;
    private String prepay_id;
    private String nonce_str;
    private String time_str;
    private Object time_sys_str;
    private String packages;
    private String paySign;
    private String payMoney;
    private int goWchatPay;
    private int payStatus;
    private Object errorMsg;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getDevice_num() {
        return device_num;
    }

    public void setDevice_num(String device_num) {
        this.device_num = device_num;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getTime_str() {
        return time_str;
    }

    public void setTime_str(String time_str) {
        this.time_str = time_str;
    }

    public Object getTime_sys_str() {
        return time_sys_str;
    }

    public void setTime_sys_str(Object time_sys_str) {
        this.time_sys_str = time_sys_str;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public int getGoWchatPay() {
        return goWchatPay;
    }

    public void setGoWchatPay(int goWchatPay) {
        this.goWchatPay = goWchatPay;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }


//    private String appid;
//    private String mch_id;//商户号
//    private String prepay_id;//预支付交易会话ID
//    private String nonce_str;//随机字符串
//    private String result_code;
//    private String return_code;
//    private String return_msg;
//    private String trade_type;
//    private String time;//时间戳
//    private String packages="Sign=WXPay";//扩展字段
//    private String sign;//签名

//    public String getAppid() {
//        return appid;
//    }
//
//    public void setAppid(String appid) {
//        this.appid = appid;
//    }
//
//    public String getMch_id() {
//        return mch_id;
//    }
//
//    public void setMch_id(String mch_id) {
//        this.mch_id = mch_id;
//    }
//
//    public String getPrepay_id() {
//        return prepay_id;
//    }
//
//    public void setPrepay_id(String prepay_id) {
//        this.prepay_id = prepay_id;
//    }
//
//    public String getNonce_str() {
//        return nonce_str;
//    }
//
//    public void setNonce_str(String nonce_str) {
//        this.nonce_str = nonce_str;
//    }
//
//    public String getResult_code() {
//        return result_code;
//    }
//
//    public void setResult_code(String result_code) {
//        this.result_code = result_code;
//    }
//
//    public String getReturn_code() {
//        return return_code;
//    }
//
//    public void setReturn_code(String return_code) {
//        this.return_code = return_code;
//    }
//
//    public String getReturn_msg() {
//        return return_msg;
//    }
//
//    public void setReturn_msg(String return_msg) {
//        this.return_msg = return_msg;
//    }
//
//    public String getTrade_type() {
//        return trade_type;
//    }
//
//    public void setTrade_type(String trade_type) {
//        this.trade_type = trade_type;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }
//
//    public String getPackages() {
//        return packages;
//    }
//
//    public void setPackages(String packages) {
//        this.packages = packages;
//    }
//
//    public String getSign() {
//        return sign;
//    }
//
//    public void setSign(String sign) {
//        this.sign = sign;
//    }
}
