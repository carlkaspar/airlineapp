package com.airlineapp.services;

import com.airlineapp.models.Location;
import com.airlineapp.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationServices {
    @Autowired
    LocationRepository locationRepository;

    public List<Location> findByName(String name) {
        return locationRepository.findByName(name);
    }

    public Location addLocation(Location location){
        return locationRepository.save(location);
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Location editLocation(Location location){
        return locationRepository.save(location);
    }

    public void deleteLocation(Location location){
        locationRepository.delete(location);
    }
}
