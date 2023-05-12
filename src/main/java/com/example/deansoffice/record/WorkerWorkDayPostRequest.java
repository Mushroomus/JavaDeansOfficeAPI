package com.example.deansoffice.record;

import java.time.LocalDate;
import java.time.LocalTime;

public record WorkerWorkDayPostRequest(LocalDate date, LocalTime startTime, LocalTime endTime) {
}
