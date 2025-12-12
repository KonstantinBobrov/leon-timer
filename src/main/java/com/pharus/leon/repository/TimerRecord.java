package com.pharus.leon.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalTime;

@Table(name = "timer", schema = "leon")
public record TimerRecord(
        @Id Long id,
        LocalTime value
) {

    public TimerRecord(LocalTime value) {
        this(null, value);
    }
}
