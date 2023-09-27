package com.tofu.demo.scheduler;

import com.tofu.demo.service.library.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoScheduler {

    @Autowired
    private LibraryService libraryService;

    @Scheduled(cron = "${demo-scheduler.cron}")
    public void trigger1() {
        libraryService.periodic1();
    }
}
