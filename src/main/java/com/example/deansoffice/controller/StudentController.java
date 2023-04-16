package com.example.deansoffice.controller;

import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.service.Manager.StudentMajorDetailsManager;
import com.example.deansoffice.service.Manager.StudentWorkDateIntervalsManager;
import com.example.deansoffice.service.StudentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private StudentWorkDateIntervalsManager studentWorkDateIntervalsManager;
    private StudentMajorDetailsManager studentMajorDetailsManager;

    StudentController(StudentService theStudentService, StudentWorkDateIntervalsManager theStudentWorkDateIntervalsManager, StudentMajorDetailsManager theStudentMajorDetailsManager) {
        studentService = theStudentService;
        studentWorkDateIntervalsManager = theStudentWorkDateIntervalsManager;
        studentMajorDetailsManager = theStudentMajorDetailsManager;
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    private ResponseEntity<String> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
        return studentService.cancelAppointment(studentId, appointmentId);
    }

    @GetMapping("/{studentId}/appointments")
    private ResponseEntity<List<Object[]>> getAppointments(@PathVariable int studentId,
                                                           @RequestParam(required = false) String startInterval, @RequestParam(required = false) String endInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           @RequestParam(required = false) Integer workerId) {
        return studentService.getAppointments(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }

    @GetMapping("/{studentId}/major-details")
    private ResponseEntity<Map<String,?>> getStudentSpecializationMajorYear(@PathVariable("studentId") int studentId) {
        return studentService.getStudentSpecializationMajorYear(studentId);
    }

    @PostMapping("/{studentId}/major-details")
    ResponseEntity<Map<String,String>> addMajorDetails(@PathVariable("studentId") Integer studentId, @RequestBody Map<String,Integer> specializationMajorYearId) {

        Integer requestBodyId = null;
        if (!specializationMajorYearId.isEmpty()) {
            requestBodyId = specializationMajorYearId.values().iterator().next();
        }
        return studentService.addMajorDetails(studentId, requestBodyId);
    }

    @PutMapping("/{studentId}/major-details/{studentMajorDetailsId}")
    ResponseEntity<Map<String,String>> editMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("studentMajorDetailsId") Integer studentMajorDetailsId, @RequestBody Map<String,Integer> majorDetailsId) {
        Integer requestBodyId = null;
        if (!majorDetailsId.isEmpty()) {
            requestBodyId = majorDetailsId.values().iterator().next();
        }
        return studentService.editMajorDetails(studentId, studentMajorDetailsId, requestBodyId);
    }

    @DeleteMapping("/{studentId}/major-details/{majorDetailsId}")
    ResponseEntity<Map<String,String>> deleteMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("majorDetailsId") Integer majorDetailsId) {
        return studentService.deleteMajorDetails(studentId, majorDetailsId);
    }

}
