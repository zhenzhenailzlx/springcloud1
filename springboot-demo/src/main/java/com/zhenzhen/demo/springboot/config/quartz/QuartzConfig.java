package com.zhenzhen.demo.springboot.config.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zhenzhen.demo.springboot.job.MyJob;

//@Configuration
public class QuartzConfig {
	
	//@Bean
    public JobDetail myJobDetail(){
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                .withIdentity("myJob1","myJobGroup1")
                .storeDurably()
                .build();
        return jobDetail;
    }
    //@Bean
    public Trigger myTrigger(){
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(myJobDetail())
                .withIdentity("myTrigger1","myTriggerGroup1")
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                .build();
        return trigger;
    }

}
