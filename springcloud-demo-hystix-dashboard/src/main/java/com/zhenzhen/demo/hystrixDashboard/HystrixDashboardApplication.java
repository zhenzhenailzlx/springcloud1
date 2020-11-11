package com.zhenzhen.demo.hystrixDashboard;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableHystrixDashboard
@RestController
public class HystrixDashboardApplication {

	@RequestMapping(value = "ping")
	public String ping(){
		return "tong"+System.currentTimeMillis();
	}

	public static void main(String[] args) {
	    new SpringApplicationBuilder(HystrixDashboardApplication.class).web(true).run(args);
	}

}
