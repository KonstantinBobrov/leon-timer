package com.pharus.leon.service;


import com.pharus.leon.dto.InfoDto;
import com.pharus.leon.repository.TimerRecord;
import com.pharus.leon.repository.TimerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.boot.health.contributor.Status;
import org.springframework.boot.jdbc.health.DataSourceHealthIndicator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

@Service
public class TimerService {
    private static final Logger logger = LoggerFactory.getLogger(TimerService.class);
    public static final String DB_TEMPORARILY_UNAVAILABLE = "The database is temporarily unavailable";

    private final HealthIndicator dataSourceHealthIndicator;
    private final BlockingQueue<TimerRecord> timerQueue;
    private final TimerRepository timerRepository;

    public TimerService(
            DataSourceHealthIndicator dataSourceHealthIndicator,
            BlockingQueue<TimerRecord> timerQueue,
            TimerRepository timerRepository
    ) {
        this.dataSourceHealthIndicator = dataSourceHealthIndicator;
        this.timerQueue = timerQueue;
        this.timerRepository = timerRepository;
    }

    @Scheduled(fixedDelay = 500)
    public void saveRecord() {
        List<TimerRecord> writeBuffer;

        try {
            if (timerQueue.size() <= 1) {
                writeBuffer = List.of(timerQueue.take());   // blocking operation
                logger.trace("Prepared a single record.");
            } else {
                writeBuffer = timerQueue.stream().toList();
                logger.trace("Prepared a batch of records.");
            }

            timerRepository.saveAll(writeBuffer);
            timerQueue.removeAll(writeBuffer);
            logger.debug("The records were saved successfully.");
        } catch (CannotCreateTransactionException e) {
            logger.warn(DB_TEMPORARILY_UNAVAILABLE, e);
        } catch (InterruptedException _) {
            logger.error("The application was interrupted");
        }
    }

    public InfoDto getTimerInfo() {
        Status dbStatus = getDataSourceStatus();

        if (dbStatus == Status.UP) {
            return new InfoDto(
                    dbStatus,
                    timerRepository.findAll()
                            .stream()
                            .map(TimerRecord::value)
                            .toList()
            );
        } else {
            logger.warn("The database is temporarily unavailable");
            return new InfoDto(
                    dbStatus,
                    Collections.emptyList()
            );
        }
    }

    private Status getDataSourceStatus() {
        return Optional.ofNullable(dataSourceHealthIndicator.health())
                .map(Health::getStatus)
                .orElse(Status.DOWN);
    }
}
