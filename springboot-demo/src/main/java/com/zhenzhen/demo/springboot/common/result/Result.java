package com.zhenzhen.demo.springboot.common.result;

import lombok.Data;

@Data
public class Result {

	//1 表示成功，0表示失败
	private String code;
	private Object data;
	private String message;
	
	public static Result success() {
		Result result = new Result();
		result.setCode("1");
		return result;
	}

	public static Result success(Object object) {
		Result result = new Result();
		result.setCode("1");
		result.setData(object);
		return result;
	}
	
	public static Result error(String code,String msg ) {
		Result result = new Result();
		result.setCode(code);
		result.setMessage(msg);
		return result;
	}
	
}
