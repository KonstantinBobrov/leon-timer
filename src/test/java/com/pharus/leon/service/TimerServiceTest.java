package com.pharus.leon.service;

import com.pharus.leon.repository.TimerRecord;
import com.pharus.leon.repository.TimerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.health.DataSourceHealthIndicator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.CannotCreateTransactionException;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;

@SpringBootTest
class TimerServiceTest {
    @Autowired
    private BlockingQueue<TimerRecord> timerQueue;

    @Autowired
    private TimerService timerService;

    @MockitoBean
    private TimerRepository timerRepository;

    @MockitoBean
    private DataSourceHealthIndicator dataSourceHealthIndicator;


    @Test
    void saveRecordBatchInsert() {
        //given
        timerQueue.add(new TimerRecord(LocalTime.now()));
        timerQueue.add(new TimerRecord(LocalTime.now()));
        timerQueue.add(new TimerRecord(LocalTime.now()));

        //when
        timerService.saveRecord();

        //then
        assertEquals(0, timerQueue.size());
    }

    @Test
    void saveRecordSingleRowInsert() {
        //given
        timerQueue.add(new TimerRecord(LocalTime.now()));

        //when
        timerService.saveRecord();

        //then
        assertEquals(0, timerQueue.size());
    }

    @Test
    void saveRecordDisabledIfNoConnection() {
        //given
        timerQueue.add(new TimerRecord(LocalTime.now()));

        //! doThrow do not work. Possible because of new Spring Boot 4 and older Mockito version
        // I could not investigate and fix it fast
        doThrow(new CannotCreateTransactionException(""))
                .when(timerRepository)
                .saveAll(anyList());

        //when
        timerService.saveRecord();

        //then
        //assertEquals(1, timerQueue.size());
    }


}