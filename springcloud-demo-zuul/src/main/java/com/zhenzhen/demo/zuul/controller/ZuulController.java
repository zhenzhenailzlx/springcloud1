package com.zhenzhen.demo.zuul.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZuulController {
	
	@RequestMapping("/getUser")
	public String getUser() {
		return "user";
	}
	
	
	@RequestMapping("/ping")
	public String getOrder() {
		 return "tong"+System.currentTimeMillis();
	}
	
	@RequestMapping("/sleep")
	public String sleep(String time) throws InterruptedException {

		if("a".equals(time)){
			throw new RuntimeException("异常降级测试");
		}

		int timeInt = 200;
		if(StringUtils.isNotEmpty(time)) {
			timeInt = Integer.parseInt(time);
		}
		Thread.sleep(timeInt);
		return "sleep"+timeInt;
	}

}
