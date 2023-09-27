package com.tofu.demo.config;

import com.tofu.demo.scheduler.job.DemoJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Configuration
public class Quartz {

    @Value("${demo-scheduler-detail.cron}")
    String cronPattern;

    @Bean
    public JobDetail jobDetail() {
        JobDataMap jobData = new JobDataMap();
        jobData.put("_source", "Quartz configuration");

        return JobBuilder.newJob(DemoJob.class).withIdentity("auto-invoke-a", "auto-invoke")
                .setJobData(jobData)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger autoTrigger() {
        return TriggerBuilder.newTrigger().forJob(jobDetail())
                .withIdentity("trigger-from-main", "auto-invoke")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronPattern))
                .startNow()
                .build();
    }

}
