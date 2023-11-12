package com.example.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyTask {

//    @Scheduled(cron = "0/5 * * * * ? ")
    public void func() {
        log.info("{}", new Date());
    }
}
