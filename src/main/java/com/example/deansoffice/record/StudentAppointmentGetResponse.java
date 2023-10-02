package com.example.deansoffice.record;

import java.time.LocalDate;
import java.time.LocalTime;

public record StudentAppointmentGetResponse(Integer appointmentId, LocalDate date, LocalTime startInterval, LocalTime endInterval, String description, String workerName, String workerSurname) {
    public StudentAppointmentGetResponse {
    }
}
