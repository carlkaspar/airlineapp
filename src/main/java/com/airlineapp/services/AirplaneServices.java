package com.airlineapp.services;

import com.airlineapp.models.Airplane;
import com.airlineapp.repositories.AirplaneRepository;
import com.airlineapp.repositories.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirplaneServices {
    @Autowired
    AirplaneRepository airplaneRepository;

    public List<Airplane> findAllAirplanes() {
        return airplaneRepository.findAll();
    }

    public Airplane addAirplane(Airplane airplane){
        return airplaneRepository.save(airplane);
    }

    public Airplane editAirplane(Airplane airplane){
        return airplaneRepository.save(airplane);
    }

    public void deleteAirplane(Airplane airplane){
        airplaneRepository.delete(airplane);
    }
}
