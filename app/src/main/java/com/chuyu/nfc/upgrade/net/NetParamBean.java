package com.chuyu.nfc.upgrade.net;

import java.util.Map;

public class NetParamBean implements java.io.Serializable{
	public String act="";
	public Map<String,Object> params=null;
	
	public String getAct() {
		if(act==null){
			act="";
		}
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	
	
}
