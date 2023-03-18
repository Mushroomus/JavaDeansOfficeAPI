package com.example.deansoffice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class NewWorkDayRequest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
