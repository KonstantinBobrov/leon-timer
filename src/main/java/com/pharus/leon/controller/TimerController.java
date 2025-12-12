package com.pharus.leon.controller;

import com.pharus.leon.dto.InfoDto;
import com.pharus.leon.service.TimerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/timer")   // can be /api/v1/timer if versioning needs.
public class TimerController {

    final TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping
    public InfoDto getTimer() {
        return timerService.getTimerInfo();
    }
}
