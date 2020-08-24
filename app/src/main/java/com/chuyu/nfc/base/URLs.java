package com.chuyu.nfc.base;

/**
 * URLs
 */
public class URLs {

    /**
     * ===============================URL=================================
     **/

    //生产环境
//    public final static String HOST_URL = "";

    //东湖高新消防救援大队外网测试环境
   public final static String HOST_URL = "http://api.caresky.cn/android";
//   public final static String HOST_URL = "http://testapi.caresky.cn/android";
   public final static String H5_URL = "file:///android_asset/web";
//   public final static String H5_URL = "http://m.caresky.cn/h5";

    public final static String APP_LOGIN="/login/applogin";//登录
    public final static String getVcode="/sms/smskey";//获取key
    public final static String getSMSCode="/sms/sendsms";//发送短信
    public final static String REGIST="/login/regist";//用户注册
    public final static String MODIFYPWD="/user/modify_pwd";// 修改密码
    public final static String HOMERECOMMEND="/home/recommend";//主页推荐
    public final static String HOMEBANNER="/home/banner";//主页Banner
    public final static String LIVEPLAY="/media/live/authkey";//获得直播鉴权 URL
    public final static String LIVEONLINE="/media/live/online";//直播状态
    public final static String MEDIACATEGORY="/media/video/categories/list";//点播分类
    public final static String MEDIACATEGORYLIST="/media/video/list";//点播分类列表
    public final static String MEDIAPLAYURL="/media/video/play";//点播播放URL



    public final static String HOMEWIKI="/home/wiki";//主页百科
    public final static String POSTCOMMENT="/common/comment/post";//发布评论
    public final static String LISTCOMMENT="/common/comment/list";// 评论列表


    public final static String FILTER="/mech/search";//机构筛选条件
    public final static String LISTINSTITUTIONS="/mech/list";//机构列表
    public final static String ADDPRODUCT="/mall/cart/add_product";// 向购物车中添加商品
    public final static String MAINCATEGORY="/mall/category";//商城主分类
    public final static String PRODUCTLIST="/mall/product_list";//商品列表
    public final static String SERVICEPERSONNEL="/o2o/index/recommend";//o2o服务人员
    public final static String RECOMMENDSTORE="/mall/recommend_store";//推荐店铺
    public final static String CATEGORIES="/mall/store_category";//店铺商品分类
    public final static String STOREINFO="/mall/store_info";// 店铺信息
    public final static String USERCENTER="/user/center";//个人中心
    public final static String UPAVATAR="/user/upload_avatar";// 上传头像

    public final static String ORDERAMOUNT="/common/order/pay";//获得订单金额
    public final static String PAYORDERDETAILS="/common/order/pay_result";//支付后详情
    public final static String ALIPAY="/common/alipay";//阿里支付info
    public final static String WETPAY="/common/wetpay";//微信支付info



    public final static String PRODUCTDETAILS="/mall/product_detail.html";//商品详情h5
    public final static String WIKIKNOW="/wiki/know.html";//知识百科h5
    public final static String WIKIKNOWDETAIL="/wiki/know_detail.html";//知识百科详情h5
//    public final static String REMOTEHOSPITAL="/hospital/doctor.html";//医生
    public final static String REMOTEHOSPITAL="/hospital/hospital.html";//远程医院h5
    public final static String HOSPITALDETAIL="/hospital/division.html";//远程医院详情h5

    public final static String UPLOADFILE="/alioss/uploadFile";//图片上传

}
