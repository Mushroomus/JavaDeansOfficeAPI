package com.example.deansoffice.controller;

import com.example.deansoffice.service.Manager.StudentWorkDateIntervalsManager;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    private StudentWorkDateIntervalsManager studentWorkDateIntervalsManager;

    StudentController(StudentWorkDateIntervalsManager theStudentWorkDateIntervalsManager) {
        studentWorkDateIntervalsManager = theStudentWorkDateIntervalsManager;
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    private ResponseEntity<String> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
        return studentWorkDateIntervalsManager.cancelAppointment(studentId, appointmentId);
    }

    @GetMapping("/{studentId}/appointments")
    private ResponseEntity<List<Object[]>> getAppointments(@PathVariable int studentId,
                                                           @RequestParam(required = false) String startInterval, @RequestParam(required = false) String endInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           @RequestParam(required = false) Integer workerId) {
        return studentWorkDateIntervalsManager.findByStudentIdAndStartInvervalAndEndInterval(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }
}
