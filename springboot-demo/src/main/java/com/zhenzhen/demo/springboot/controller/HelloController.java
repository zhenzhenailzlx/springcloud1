package com.zhenzhen.demo.springboot.controller;

import java.util.Date;

import io.prometheus.client.Counter;
import io.prometheus.client.Histogram;
import io.prometheus.client.SimpleTimer;
import io.prometheus.client.Summary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhenzhen.demo.springboot.common.utils.BeanValidatorUtil;
import com.zhenzhen.demo.springboot.condition.HelloCondition;
import com.zhenzhen.demo.springboot.dto.HelloDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;

@RestController
@Api(description = "hello的控制器")
@Log
public class HelloController {

	private static final Counter Hello_Counter = Counter.build()
			.name("hello_counter").help("hello_counter").register();

	static final Summary requestLatency = Summary.build()
			.name("requests_latency_seconds")
			.help("Request latency in seconds.")
			.labelNames("aLabel")
			.register();

	static final Histogram requestHistogramLatency = Histogram.build()
			.name("requests_histogram_latency_seconds").help("Request latency in seconds.").register();

	@GetMapping("/hello")
	@ApiOperation(value="hello 方法")
	public HelloDto hello(HelloCondition helloCondition) {
		SimpleTimer requestTimer = new SimpleTimer();
		Histogram.Timer requestHistogramTimer = requestHistogramLatency.startTimer();

		try {
			Hello_Counter.inc();

			BeanValidatorUtil.check(helloCondition);
			log.info("输入参数"+helloCondition);
			return new HelloDto("真哥", new Date());
		} finally {
			requestHistogramTimer.observeDuration();
			//这里可以根据参数输入lable
			requestLatency.labels("aLabelValue").observe(requestTimer.elapsedSeconds());
		}
	}
}
