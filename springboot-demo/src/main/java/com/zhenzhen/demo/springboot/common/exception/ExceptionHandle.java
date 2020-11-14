package com.zhenzhen.demo.springboot.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhenzhen.demo.springboot.common.result.Result;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionHandle {


	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result handle(Exception e) {
		if(e instanceof SprintBootDemoException) {
			SprintBootDemoException sprintBootException = (SprintBootDemoException)e;
			return Result.error(sprintBootException.getCode(), sprintBootException.getMessage());
		}else{
			log.error(e.toString());
			return Result.error("-1", "未知错误");
		}
	}
}
