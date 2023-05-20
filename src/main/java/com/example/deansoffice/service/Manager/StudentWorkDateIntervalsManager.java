package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentWorkDateIntervalsManager {
    ResponseEntity<Response> cancelAppointment(Integer workerId, Integer studentId, Integer appointmentId);
    ResponseEntity<List<StudentAppointmentGetResponse>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString, LocalDate startDate, LocalDate endDate, Integer workerId);
    void saveWorkDateInterval(WorkDateIntervals workDateIntervals);
}
