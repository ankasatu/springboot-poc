package com.tofu.demo.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
public class DemoJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        log.info("DemoJob executed at: {}, ", ZonedDateTime.now());

        dataMap.keySet().stream().sorted().forEach(k-> log.info("{} : {}", k, dataMap.get(k)));
    }
}
