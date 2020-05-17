package com.airlineapp.repositories;

import com.airlineapp.models.Airport;
import com.airlineapp.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>{
    List<Airport> findAllByScheduleId(Long scheduleId);
    List<Airport> findAllByName(String name);
    Optional<Reservation> findByReservationCode(String reservationCode);
}
