package com.zhenzhen.demo.springboot;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.zhenzhen.demo.springboot.job.MyJob;

public class QuartzTest {
	
	public static void main(String[] args) {
		
        try {
            // Grab the Scheduler instance from the Factory 
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // and start it off
            scheduler.start();
            
            // define the job and tie it to our HelloJob class
            JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                    .withIdentity("myJob1","myJobGroup1")
                    .usingJobData("testkey","testValue")
                    .usingJobData("testkey2",1)
                    .storeDurably()
                    .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withIdentity("myTrigger1","myTriggerGroup1")
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
                    .build();

            // Tell quartz to schedule the job using our trigger
            scheduler.scheduleJob(jobDetail, trigger);

            //scheduler.shutdown();

        } catch (SchedulerException se) {
            se.printStackTrace();
        }
	}

}
