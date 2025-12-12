package com.pharus.leon.dto;

import org.springframework.boot.health.contributor.Status;

import java.time.LocalTime;
import java.util.List;

public record InfoDto(
        Status dbStatus,
        List<LocalTime> timer
) {
}
