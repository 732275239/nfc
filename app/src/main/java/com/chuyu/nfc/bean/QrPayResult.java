package com.chuyu.nfc.bean;

/**
 * Created by Zoello on 2018/12/11.
 * 支付的回调
 */

public class QrPayResult {

    /**
     * type : mechanism
     * order_no : 2015447686925340001
     * occupant_names : 大哥;
     * pay_amount : 0.010
     * pay_method : 待支付
     * pay_time : 2018-12-14 14:25:08.0
     */

    private String type;
    private String order_no;
    private String occupant_names;
    private String pay_amount;
    private String pay_method;
    private String pay_time;
    /**
     * user_name : 王大锤
     * user_phone : 13800138000
     * user_address : 湖北省武汉市洪山区光谷世贸J栋9楼
     */

    private String user_name;
    private String user_phone;
    private String user_address;
    /**
     * type_name : 家政服务
     * type_cate_name : 中医理疗
     * begin_time : 2018-12-16 11:51:00.0
     * end_time : 2018-12-18 11:51:00.0
     */

    private String type_name;
    private String type_cate_name;
    private String begin_time;
    private String end_time;
    /**
     * payment_time :
     * order_amount : 579.00
     * receive_name : jjjsjd
     * status_str : 待支付
     * receive_tel : 13645464649
     * id : 418
     * payment_method : 待支付
     * receive_address : hxhxjxj
     * status : 0
     */

    private String payment_time;
    private String order_amount;
    private String receive_name;
    private String status_str;
    private String receive_tel;
    private String id;
    private String payment_method;
    private String receive_address;
    private String status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOccupant_names() {
        return occupant_names;
    }

    public void setOccupant_names(String occupant_names) {
        this.occupant_names = occupant_names;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getPay_method() {
        return pay_method;
    }

    public void setPay_method(String pay_method) {
        this.pay_method = pay_method;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_cate_name() {
        return type_cate_name;
    }

    public void setType_cate_name(String type_cate_name) {
        this.type_cate_name = type_cate_name;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getStatus_str() {
        return status_str;
    }

    public void setStatus_str(String status_str) {
        this.status_str = status_str;
    }

    public String getReceive_tel() {
        return receive_tel;
    }

    public void setReceive_tel(String receive_tel) {
        this.receive_tel = receive_tel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
