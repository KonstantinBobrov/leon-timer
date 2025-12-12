package com.pharus.leon.service;


import com.pharus.leon.repository.TimerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

@Service
public class TimerAdderScheduledService {
    private static final Logger logger = LoggerFactory.getLogger(TimerAdderScheduledService.class);

    final BlockingQueue<TimerRecord> timerQueue;

    public TimerAdderScheduledService(BlockingQueue<TimerRecord> timerQueue) {
        this.timerQueue = timerQueue;
    }

    @Scheduled(fixedRate = 1000)
    public void addRecord() {
        logger.trace("Offering a new time record");
        boolean offer = timerQueue.offer(new TimerRecord(LocalTime.now()));
        if (!offer) {
            logger.error("Critical error. The timer queue is full, the sequence is broken.");
        }
    }
}
