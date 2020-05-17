package com.airlineapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SearchParameters {
    private boolean returnFlight = false;
    private String fromAirport;
    private String toAirport;
    private String searchInputFrom;
    private String searchInputTo;
    private Long fromLocationId;
    private Long toLocationId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate flightTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnFlightTime;
    @Min(value = 1)
    private Integer numberOfSeats;

    public SearchParameters(String fromAirport, String toAirport, LocalDate flightTime, @Min(value = 1) Integer numberOfSeats) {
        this.fromAirport = fromAirport;
        this.toAirport = toAirport;
        this.flightTime = flightTime;
        this.numberOfSeats = numberOfSeats;
    }
}
