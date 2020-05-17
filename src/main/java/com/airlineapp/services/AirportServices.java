package com.airlineapp.services;

import com.airlineapp.models.Airport;
import com.airlineapp.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServices {

    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> findAllByCode(String code){
        return airportRepository.findAllByCode(code);
    }

    List<Airport> findAllByName(String name){
        return airportRepository.findAllByName(name);
    }

    List<Airport> findAllByLocationId(Long locationId){
        return airportRepository.findAllByLocationId(locationId);
    }

    List<Airport> findAllById(Long id){
        return airportRepository.findAllById(id);
    }

    public Airport addAirport(Airport airport){
        return airportRepository.save(airport);
    }

    public void deleteAirport(Airport airport){
        airportRepository.delete(airport);
    }

    public List<Airport> findAllAirports() {
        return airportRepository.findAll();
    }

    public Airport editAirport(Airport airport){
        return airportRepository.save(airport);
    }
}
