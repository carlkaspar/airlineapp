package com.airlineapp.exeptions;

import lombok.Getter;

@Getter
public class ScheduleCapacityException extends RuntimeException {
    private Long scheduleId;
    private Integer numberToReduce;

    public ScheduleCapacityException(String message, Long scheduleId, Integer numberToReduce) {
        super(message);
        this.scheduleId = scheduleId;
        this.numberToReduce = numberToReduce;
    }
}
