package com.chuyu.nfc.upgrade.net;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.nfc.upgrade.appconfig.AppConfig;
import com.chuyu.nfc.upgrade.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xxu on 2018/5/23.
 */
public class NetRequest {


    /**
     * 创建任意参数 (不带用户参数)
     *
     * @param act
     * @param params
     * @return
     */
    public static NetParamBean create(String act, Map<String, Object> params) {
        NetParamBean paramBean = new NetParamBean();
        if (params == null) {
            params = new HashMap<String, Object>();
        }
        paramBean.setParams(params);
        paramBean.setAct(act);
        return paramBean;
    }


    public static void post(NetParamBean param,final NetConnectTask netConnectTask){

        try{
            if(param==null || netConnectTask==null){
                return;
            }
            String apiUrl="";
            if(param.getAct().startsWith("http://") || param.getAct().startsWith("https://")){
                apiUrl=param.getAct();
            }else{
                apiUrl= AppConfig.NET_HOST_URL + param.getAct();
            }
            RequestParams params = new RequestParams(apiUrl);
            Map<String, Object> paramMap=param.getParams();
            if(paramMap!=null && !paramMap.isEmpty()){
                for (Map.Entry<String, Object> entryParam : paramMap.entrySet()) {
                    params.addParameter(entryParam.getKey(),entryParam.getValue());
                }
            }
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        if(AppConfig.DEBUG_ENABLE){
                            Log.d(AppConfig.DEBUG_TAG,"接口返回："+result);
                        }
                        if(TextUtils.isEmpty(result)){
                            netConnectTask.onFailure(NetErrorCode.FAIL, "接口无返回数据！");
                            return;
                        }
                        JSONObject response = new JSONObject(result);
                        int rsCode = response.optInt("code");
                        String eMsg = response.optString("msg");

                        if (rsCode == NetErrorCode.SUCCEED) {// 成功
                            try {
                                JSONObject rsSingleBean = response.optJSONObject("dataSingle");
                                JSONArray rsListBean = response.optJSONArray("data");
                                if (rsSingleBean != null) {
                                    Object t = JsonUtil.jsonToResult(rsSingleBean.toString(), netConnectTask.doGetBackClass());
                                    netConnectTask.onSuccess(t, rsCode,eMsg);
                                } else if (rsListBean != null) {
                                    Object t = JsonUtil.jsonToResult(rsListBean.toString(), netConnectTask.doGetBackClass());
                                    netConnectTask.onSuccess(t,rsCode, eMsg);
                                } else {
                                    BackResult netResult = new BackResult();
                                    netResult.setCode(rsCode);
                                    netResult.setMsg(eMsg);
                                    netConnectTask.onSuccess(netResult,rsCode, eMsg);
                                }
                            } catch (Exception e) {// 失败
                                e.printStackTrace();
                                netConnectTask.onFailure(NetErrorCode.FAIL,"数据解析失败！");
                            }
                        } else {// 失败
                            netConnectTask.onFailure(rsCode, eMsg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        netConnectTask.onFailure(NetErrorCode.FAIL, "网络数据解析失败！");
                    }

                }

                //请求异常后的回调方法
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    netConnectTask.onFailure(NetErrorCode.FAIL, "网络连接错误，请稍后重试！");
                }

                //主动调用取消请求的回调方法
                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }





    }

}
