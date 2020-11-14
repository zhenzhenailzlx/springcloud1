package com.zhenzhen.demo.springboot.common.constants;

public enum ResultEunm {
	UNKONW_ERROR(-1,"未知错误"),
	SUCCESS(0,"成功"),
	;
	private Integer code;
	private String msg;
	private ResultEunm(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
