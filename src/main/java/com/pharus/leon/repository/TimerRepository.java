package com.pharus.leon.repository;

import org.springframework.data.repository.ListCrudRepository;

public interface TimerRepository extends ListCrudRepository<TimerRecord, Long> {
}
