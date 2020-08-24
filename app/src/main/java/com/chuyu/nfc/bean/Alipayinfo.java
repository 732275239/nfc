package com.chuyu.nfc.bean;

/**
 * Created by Zoello on 2018/12/8.
 */

public class Alipayinfo {

    /**
     * orderInfo : alipay_sdk=alipay-sdk-java-3.3.2&app_id=2018111362141805&biz_content=%7B%22body%22%3A%22%E6%B5%8B%E8%AF%95%E6%94%AF%E4%BB%98%E5%AE%9D%22%2C%22out_trade_no%22%3A%22201812080204171977262%22%2C%22passback_params%22%3A%22%25E6%25B5%258B%25E8%25AF%2595%25E6%2594%25AF%25E4%25BB%2598%25E5%25AE%259D%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%93%81%E8%B4%AD%E4%B9%B0%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fapi.caresky.cn%2Falipay%2Fnotify_url&sign=E3DcE6a4poFA2uvxtpeYE5ncHaS2IkRJQo796ViWuK7zpkHiC7GTDbrsY%2B8zLbwaf222XPYoyr3qmgpSBMjxkLJyeevp1jAYWeYKejr%2FSXRab6%2BTxBxWm5jJbmgG5r%2FHmrUWP87MfThnDJT1yVmm8NggapMI1gy2bBoZ6lkfXtMAN%2BBKFHQoC2DIzHVVNAbeJPvsbz7mxy%2B2b9T2du064D3kHM4%2FgrBsCEDRRRfeCQ7X%2BGtYptza7iBBNGocOcWgjHZfzxSqiW5DZheMmIgbBKQporMXNdVpnsU11Yk7IOoYL56JhCfJxAIaWu0XyO%2FYZtIahJdo4825EXJfoHDP5w%3D%3D&sign_type=RSA2&timestamp=2018-12-08+14%3A17%3A45&version=1.0
     */

    private String orderInfo;

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }
}
