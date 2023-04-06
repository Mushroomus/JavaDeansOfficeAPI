package com.example.deansoffice.controller;

import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.model.RegisterStudent;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private StudentMajorDetailsService studentMajorDetailsService;
    private SpecializationService specializationService;
    private SpecializationMajorYearService specializationMajorYearService;
    private WorkDateIntervalsService workDateIntervalsService;

    StudentController(@Qualifier("studentServiceImpl") StudentService theStudentService, @Qualifier("studentMajorDetailsServiceImpl") StudentMajorDetailsService theStudentMajorDetailsService,
                      SpecializationService theSpecializationService, SpecializationMajorYearService theSpecializationMajorYearService, WorkDateIntervalsService theWorkDateIntervalsService) {
        studentService = theStudentService;
        studentMajorDetailsService = theStudentMajorDetailsService;
        specializationService = theSpecializationService;
        specializationMajorYearService = theSpecializationMajorYearService;
        workDateIntervalsService = theWorkDateIntervalsService;
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    private ResponseEntity<String> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
        return workDateIntervalsService.cancelAppointment(studentId, appointmentId);
    }

    @GetMapping("/{studentId}/appointments")
    private ResponseEntity<List<Object[]>> getAppointments(@PathVariable int studentId,
                                                           @RequestParam(required = false) String startInterval, @RequestParam(required = false) String endInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           @RequestParam(required = false) Integer workerId) {
        return workDateIntervalsService.findByStudentIdAndStartInvervalAndEndInterval(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }
}
