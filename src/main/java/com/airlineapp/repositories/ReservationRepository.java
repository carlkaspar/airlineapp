package com.airlineapp.repositories;

import com.airlineapp.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByScheduleId(Long scheduleId);
    List<Reservation> findAllByName(String name);
    Optional<Reservation> findByBookingCode(String bookingCode);
}
