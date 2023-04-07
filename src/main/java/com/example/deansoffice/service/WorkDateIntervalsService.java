package com.example.deansoffice.service;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.model.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface WorkDateIntervalsService {
    void createIntervals(WorkDate workDate, int interval_minutes);

    void saveWorkDateInterval(WorkDateIntervals workDateIntervals);

    ResponseEntity<String> cancelAppointment(Integer studentId, Integer appointmentId);

    ResponseEntity<List<Object[]>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString,
                                                                                 LocalDate startDateLong, LocalDate endDateLong, Integer workerId);

    Optional<WorkDateIntervals> findWorkDateIntervalById(Integer workDateIntervalId);

    List<Pair<LocalTime, LocalTime>> getIntervalsByWorkerIdAndWorkDateId(int workerId, int workDateId);
}
