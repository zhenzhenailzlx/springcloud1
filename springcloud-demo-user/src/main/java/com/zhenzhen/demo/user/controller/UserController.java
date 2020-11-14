package com.zhenzhen.demo.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	
	private static Semaphore semaphore = new Semaphore(2);
	
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
		semaphore.acquire(1);

		int timeInt = 200;
		if(StringUtils.isNotEmpty(time)) {
			timeInt = Integer.parseInt(time);
		}
		Thread.sleep(timeInt);

		semaphore.release();
		return "sleep"+timeInt;
	}

}
