package com.example.deansoffice.controller;

import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.StudentMajorDetailsPostAndPutRequest;
import com.example.deansoffice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    StudentController(@Qualifier("studentServiceImpl") StudentService theStudentService) {
        studentService = theStudentService;
    }

    @GetMapping("/{studentId}")
    private StudentDTO getStudent(@PathVariable("studentId") Integer studentId) {
        return studentService.getStudent(studentId);
    }

    @PutMapping("/{studentId}")
    private ResponseEntity<Response> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(studentId, StudentDTO.toEntity(studentDTO));
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    @Operation(summary = "Cancel Student Appointment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Get Appointment",
                            content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "500", description = "Failed to save",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to save\"\n}"))),
            })
    private ResponseEntity<Response> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
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
    ResponseEntity<Response> addMajorDetails(@PathVariable("studentId") Integer studentId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.addMajorDetails(studentId,  studentMajorDetailsRequest.specializationMajorYearId());
    }

    @PutMapping("/{studentId}/major-details/{studentMajorDetailsId}")
    ResponseEntity<Response> editMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("studentMajorDetailsId") Integer studentMajorDetailsId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.editMajorDetails(studentId, studentMajorDetailsId, studentMajorDetailsRequest.specializationMajorYearId());
    }

    @DeleteMapping("/{studentId}/major-details/{majorDetailsId}")
    ResponseEntity<Response> deleteMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("majorDetailsId") Integer majorDetailsId) {
        return studentService.deleteMajorDetails(studentId, majorDetailsId);
    }

}
