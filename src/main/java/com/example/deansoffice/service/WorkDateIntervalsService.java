package com.example.deansoffice.service;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.model.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface WorkDateIntervalsService {

    ResponseEntity<String> cancelAppointment(Integer studentId, Integer appointmentId);

    ResponseEntity<List<Object[]>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString,
                                                                                 LocalDate startDateLong, LocalDate endDateLong, Integer workerId);
    Optional<WorkDateIntervals> findWorkDateIntervalById(Integer workDateIntervalId);

    ResponseEntity<String> deleteWorkDate(int id);

    ResponseEntity<String> deleteListOfWorkDatesIntervals(@RequestBody List<Integer> workDatesIntervalsListId);
}
