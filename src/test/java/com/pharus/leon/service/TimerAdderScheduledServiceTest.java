package com.pharus.leon.service;

import com.pharus.leon.repository.TimerRecord;
import com.pharus.leon.repository.TimerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TimerAdderScheduledServiceTest {
    @Autowired
    private BlockingQueue<TimerRecord> timerQueue;

    @Autowired
    private TimerAdderScheduledService timerAdderScheduledService;

    @Test
    void addRecordSuccess() {
        timerAdderScheduledService.addRecord();
        timerAdderScheduledService.addRecord();
        timerAdderScheduledService.addRecord();

        assertEquals(3, timerQueue.size());
    }
}