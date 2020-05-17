package com.airlineapp.services;

import com.airlineapp.models.Reservation;
import com.airlineapp.models.ReservationStatus;
import com.airlineapp.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ReservationServices {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ScheduleServices scheduleServices;

    public Reservation addReservation(Reservation reservation){
        reservation.setStatus(ReservationStatus.BOOKED);
        reservation.setReservationCode(generateReservationCode());


        //TODO prevent infinite loop, if all reservation codes in use
        while (checkIfReservationCodeExists(reservation.getReservationCode())) {
            reservation.setReservationCode(generateReservationCode());
        }

        Long scheduleId = reservation.getScheduleId();
        scheduleServices.reduceScheduleCapacity(scheduleId, 1);

        return reservationRepository.save(reservation);

    }

    public Reservation editReservationName(Reservation reservation) {
        Reservation changedReservation = reservationRepository.findByReservationCode(reservation.getReservationCode()).get();
        changedReservation.setName(reservation.getName());
        return reservationRepository.save(changedReservation);
    }

    public Reservation cancelReservation(Reservation reservation) {
        Reservation cancelledReservation = reservationRepository.findByReservationCode(reservation.getReservationCode()).get();
        if(cancelledReservation.getStatus().equals(ReservationStatus.CANCELLED)) {
            return cancelledReservation;
        } else {
            cancelledReservation.setStatus(ReservationStatus.CANCELLED);
            return reservationRepository.save(cancelledReservation);
        }
    }

    public Reservation getByReservationCode(String code) {
        return reservationRepository.findByReservationCode(code).get();
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }




    private boolean checkIfReservationCodeExists(String reservationCode) {
        return reservationRepository.findByReservationCode(reservationCode).isPresent();
    }


    private String generateReservationCode() {
        int length = 8;
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                + "abcdefghijklmnopqrstuvwxyz"
                                + "0123456789";

        String reservationCode = new Random().ints(length, 0, chars.length())
                    .mapToObj(i -> "" + chars.charAt(i)).collect(Collectors.joining());
        return reservationCode;
    }


}
