package com.zhenzhen.demo.springboot.common.exception;

public class SprintBootDemoException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1092798459937082802L;

	private String code;
	
	public SprintBootDemoException(String code,String msg) {
		super(msg);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
