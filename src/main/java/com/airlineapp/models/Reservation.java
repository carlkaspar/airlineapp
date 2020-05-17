package com.airlineapp.models;


import lombok.*;

import javax.persistence.*;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long scheduleId;
    private String name;
    private String reservationCode;
    @Enumerated(value = EnumType.STRING)
    private ReservationStatus status;
}
