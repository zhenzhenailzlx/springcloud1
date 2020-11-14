package com.zhenzhen.demo.springboot.job;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJob extends QuartzJobBean{

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("开始执行"+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        try {
            for(;;) {
            	Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("结束执行"+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
	}

}
