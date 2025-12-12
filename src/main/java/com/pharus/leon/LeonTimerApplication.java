package com.pharus.leon;

import com.pharus.leon.repository.TimerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.health.DataSourceHealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@EnableScheduling
@SpringBootApplication
public class LeonTimerApplication {

    static void main(String[] args) {
        SpringApplication.run(LeonTimerApplication.class, args);
    }


    @Bean
    public BlockingQueue<TimerRecord> timerQueue() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public DataSourceHealthIndicator sqlDataSourceHealthIndicator(DataSource dataSource) {
        return new DataSourceHealthIndicator(dataSource);
    }

}
