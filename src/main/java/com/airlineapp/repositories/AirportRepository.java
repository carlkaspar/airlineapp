package com.airlineapp.repositories;

import com.airlineapp.models.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    List<Airport> findAllByCode(String code);
    List<Airport> findAllByName(String name);
    List<Airport> findAllByLocationId(Long locationId);
    List<Airport> findAllById(Long id);
}
