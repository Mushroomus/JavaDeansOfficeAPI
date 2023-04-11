package com.example.deansoffice.service.Manager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentWorkDateIntervalsManager {
    ResponseEntity<String> cancelAppointment(Integer studentId, Integer appointmentId);
    ResponseEntity<List<Object[]>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString, LocalDate startDate, LocalDate endDate, Integer workerId);
}
