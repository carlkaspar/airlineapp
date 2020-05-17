package com.airlineapp.repositories;

import com.airlineapp.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(
            String fromAirportCode,
            String toAirportCode,
            LocalDateTime flightTimeFrom,
            LocalDateTime flightTimeTo,
            Integer numberOfPeople
    );
}
