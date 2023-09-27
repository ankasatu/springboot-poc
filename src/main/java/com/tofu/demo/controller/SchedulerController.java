package com.tofu.demo.controller;

import com.tofu.demo.scheduler.job.DemoJob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/scheduler")
@AllArgsConstructor
public class SchedulerController {
    private Scheduler scheduler;

    @GetMapping("/zoned-date-time")
    public ResponseEntity<String> getZonedDateTime(@RequestParam int second) {
        return ResponseEntity.ok(ZonedDateTime.now().plusSeconds(second).toString());
    }

    @PostMapping("/exec")
    public ResponseEntity<Object> spawnScheduler(@RequestBody SchedulerRequest request) throws SchedulerException {

        ZonedDateTime startAt = request.getStartAt();
        String identifier = UUID.randomUUID().toString();

        // create jobdetail
        JobDataMap payload = new JobDataMap();
        payload.putAll(request.payload);
        payload.put("_identifier", identifier);

        JobDetail jobDetail = JobBuilder.newJob(DemoJob.class)
                .withIdentity(identifier, request.getGroupName())
                .setJobData(payload).build();

        // create trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();

        // spawn
        scheduler.scheduleJob(jobDetail, trigger);

        log.info("Spawn {} at {}", identifier, startAt);

        return ResponseEntity.ok(null);
    }

    @Getter
    public static class SchedulerRequest {
        private ZonedDateTime startAt;
        private String groupName = "quartz-kitchen-demo";
        private HashMap<String, String> payload = new HashMap<>();
    }
}
