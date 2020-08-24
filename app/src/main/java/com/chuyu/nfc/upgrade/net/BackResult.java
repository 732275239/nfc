package com.chuyu.nfc.upgrade.net;

public class BackResult implements java.io.Serializable{
	
	private int code;
	private String msg;


	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
