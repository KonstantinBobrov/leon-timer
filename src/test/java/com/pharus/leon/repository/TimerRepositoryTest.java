package com.pharus.leon.repository;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJdbcTest
@Testcontainers
class TimerRepositoryTest {

    @Autowired
    TimerRepository timerRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void contextLoads() {
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void getNothingFromInitialRepo() {
        //given and when
        List<TimerRecord> beforeInserts = timerRepository.findAll();

        //then
        assertEquals(0, beforeInserts.size());
    }

    @Test
    void saveTwoRecordsAndFindThem() {
        //given
        timerRepository.saveAll(List.of(
                new TimerRecord(LocalTime.now()),
                new TimerRecord(LocalTime.now().plusSeconds(1))));

        //when
        List<TimerRecord> afterInserts = timerRepository.findAll();

        //then
        assertEquals(2, afterInserts.size());
    }

}