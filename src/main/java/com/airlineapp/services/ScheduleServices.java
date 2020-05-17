package com.airlineapp.services;

import com.airlineapp.exeptions.ScheduleCapacityException;
import com.airlineapp.models.Airport;
import com.airlineapp.models.Schedule;
import com.airlineapp.models.SearchParameters;
import com.airlineapp.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ScheduleServices {

    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    AirportServices airportServices;

    public List<Schedule> searchForAvailableSchedules(SearchParameters searchParameters){
        Optional<Long> fromLocationId = Optional.ofNullable(searchParameters.getFromLocationId());
        Optional<Long> toLocationId = Optional.ofNullable(searchParameters.getFromLocationId());
        LocalDateTime flightTimeFrom = searchParameters.getFlightTime().atStartOfDay();
        LocalDateTime flightTimeTo = flightTimeFrom.plusDays(1).minusSeconds(1);

        if (fromLocationId.isPresent() && toLocationId.isPresent()) {
            List<Airport> fromAirports = airportServices.findAllByLocationId(fromLocationId.get());
            List<Airport> toAirports = airportServices.findAllByLocationId(toLocationId.get());
            List<Schedule> schedules = new ArrayList<>();

            for (Airport fromAirport: fromAirports) {
                for (Airport toAirport: toAirports) {
                    schedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(fromAirport.getCode(),
                            toAirport.getCode(),flightTimeFrom,flightTimeTo, searchParameters.getNumberOfSeats()));
                }
            }

            return schedules;

        } else if (fromLocationId.isPresent()) {

            List<Airport> fromAirports = airportServices.findAllByLocationId(fromLocationId.get());
            List<Schedule> schedules = new ArrayList<>();
            for (Airport fromAirport: fromAirports) {
                schedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(fromAirport.getCode(),
                        searchParameters.getToAirport(), flightTimeFrom, flightTimeTo,searchParameters.getNumberOfSeats()));
            }
            return schedules;

        } else if (toLocationId.isPresent()) {

            List<Airport> toAirports = airportServices.findAllByLocationId(toLocationId.get());
            List<Schedule> schedules = new ArrayList<>();
            for (Airport toAirport: toAirports) {
                schedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(searchParameters.getFromAirport(),
                        toAirport.getCode(), flightTimeFrom, flightTimeTo,searchParameters.getNumberOfSeats()));
            }
            return schedules;

        } else {
            return scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(searchParameters.getFromAirport(),
                    searchParameters.getToAirport(), flightTimeFrom, flightTimeTo, searchParameters.getNumberOfSeats());
        }
    }

    public Schedule getScheduleById (Long scheduleId){
        return scheduleRepository.findById(scheduleId).get();
    }

    public void reduceScheduleCapacity (Long scheduleId, int numberToReduce){
        Schedule schedule = getScheduleById(scheduleId);
        Long remainingCapacity = schedule.getRemCapacity();
        if(remainingCapacity < numberToReduce) {
            throw new ScheduleCapacityException("Flight is fully booked", scheduleId, numberToReduce);
        }
        schedule.setRemCapacity(remainingCapacity - numberToReduce);
        scheduleRepository.save(schedule);
    }

    public Map<String, List<Schedule>> searchForFlightSchedulesWithReturn(SearchParameters searchParameters) {
        Optional<Long> fromLocationId = Optional.ofNullable(searchParameters.getFromLocationId());
        Optional<Long> toLocationId = Optional.ofNullable(searchParameters.getToLocationId());
        LocalDateTime flightTimeFrom = searchParameters.getFlightTime().atStartOfDay();
        LocalDateTime flightTimeTo = flightTimeFrom.plusDays(1).minusSeconds(1);
        LocalDateTime returnflightTimeFrom = searchParameters.getReturnFlightTime().atStartOfDay();
        LocalDateTime returnflightTimeTo = returnflightTimeFrom.plusDays(1).minusSeconds(1);
        Map<String, List<Schedule>> flightsAndReturnFlights = new HashMap<>();
        List<Schedule> toSchedules = new ArrayList<>();
        List<Schedule> returnSchedules = new ArrayList<>();

        if (fromLocationId.isPresent() && toLocationId.isPresent()) {
            List<Airport> fromAirports = airportServices.findAllByLocationId(fromLocationId.get());
            List<Airport> toAirports = airportServices.findAllByLocationId(toLocationId.get());



            for (Airport fromAirport: fromAirports) {
                for (Airport toAirport: toAirports) {
                    toSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(fromAirport.getCode(),
                            toAirport.getCode(),flightTimeFrom,flightTimeTo, searchParameters.getNumberOfSeats()));
                    returnSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(toAirport.getCode(),
                            fromAirport.getCode(),returnflightTimeFrom,returnflightTimeTo,searchParameters.getNumberOfSeats()));
                }
            }
            flightsAndReturnFlights.put("toFlights", toSchedules);
            flightsAndReturnFlights.put("returnFlights", returnSchedules);

            return flightsAndReturnFlights;

        } else if (fromLocationId.isPresent()) {
            List<Airport> fromAirports = airportServices.findAllByLocationId(fromLocationId.get());
            for (Airport fromAirport: fromAirports) {
                toSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(fromAirport.getCode(),
                        searchParameters.getToAirport(), flightTimeFrom, flightTimeTo,searchParameters.getNumberOfSeats()));
                returnSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(
                        searchParameters.getToAirport(), fromAirport.getCode(), returnflightTimeFrom, returnflightTimeTo, searchParameters.getNumberOfSeats()
                ));
            }

            flightsAndReturnFlights.put("toFlights", toSchedules);
            flightsAndReturnFlights.put("returnFlights", returnSchedules);
            return flightsAndReturnFlights;
        } else if (toLocationId.isPresent()) {
            List<Airport> toAirports = airportServices.findAllByLocationId(toLocationId.get());
            for (Airport toAirport: toAirports) {
                toSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(searchParameters.getFromAirport(),
                        toAirport.getCode(), flightTimeFrom, flightTimeTo,searchParameters.getNumberOfSeats()));
                returnSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(toAirport.getCode(),
                        searchParameters.getFromAirport(), returnflightTimeFrom, returnflightTimeTo,searchParameters.getNumberOfSeats()));
            }
            flightsAndReturnFlights.put("toFlights", toSchedules);
            flightsAndReturnFlights.put("returnFlights", returnSchedules);
            return flightsAndReturnFlights;
        } else {
            toSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(searchParameters.getFromAirport(),
                    searchParameters.getToAirport(), flightTimeFrom, flightTimeTo,searchParameters.getNumberOfSeats()));
            returnSchedules.addAll(scheduleRepository.findAllByFromAirportCodeAndToAirportCodeAndDeptTimeAndRemCapacityGreaterThanEqual(
                    searchParameters.getToAirport(), searchParameters.getFromAirport(), returnflightTimeFrom, returnflightTimeTo,searchParameters.getNumberOfSeats()));
            flightsAndReturnFlights.put("toFlights", toSchedules);
            flightsAndReturnFlights.put("returnFlights", returnSchedules);
            return flightsAndReturnFlights;
        }

    }

    public Schedule addSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Schedule schedule){ scheduleRepository.delete(schedule);}

    public Schedule editSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }


    public List<Schedule> findAllSchedules() {
        return scheduleRepository.findAll();
    }

}
