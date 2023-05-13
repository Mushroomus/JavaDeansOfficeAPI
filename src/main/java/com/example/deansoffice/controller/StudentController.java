package com.example.deansoffice.controller;

import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.model.ErrorResponse;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import com.example.deansoffice.record.StudentMajorDetailsPostAndPutRequest;
import com.example.deansoffice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Student", description = "Endpoints for student actions")
@SecurityRequirement(name = "bearerAuth")
public class StudentController {

    private final StudentService studentService;

    StudentController(@Qualifier("studentServiceImpl") StudentService theStudentService) {
        studentService = theStudentService;
    }

    @GetMapping("/{studentId}")
    @Operation(summary = "Get student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = StudentDTO.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    private StudentDTO getStudent(@PathVariable("studentId") Integer studentId) {
        return studentService.getStudent(studentId);
    }

    @PutMapping("/{studentId}")
    @Operation(summary = "Update student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to update student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    private ResponseEntity<Response> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(studentId, StudentDTO.toEntity(studentDTO));
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    @Operation(summary = "Cancel student appointment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment canceled", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Appointment not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to cancel appointment", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    private ResponseEntity<Response> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
        return studentService.cancelAppointment(studentId, appointmentId);
    }

    @GetMapping("/{studentId}/appointments")
    @Operation(summary = "Get student appointments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Appointment canceled", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentAppointmentGetResponse.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Failed to parse date", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get student appointments", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    private ResponseEntity<List<StudentAppointmentGetResponse>> getAppointments(@PathVariable int studentId,
                                                           @RequestParam(required = false) String startInterval, @RequestParam(required = false) String endInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           @RequestParam(required = false) Integer workerId) {
        return studentService.getAppointments(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }

    @GetMapping("/{studentId}/major-details")
    @Operation(summary = "Get student appointments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SpecializationMajorYearDTO.class)))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get student specialization major years", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    private ResponseEntity<List<SpecializationMajorYearDTO>> getStudentSpecializationMajorYear(@PathVariable("studentId") int studentId) {
        return studentService.getStudentSpecializationMajorYear(studentId);
    }

    @PostMapping("/{studentId}/major-details")
    @Operation(summary = "Add major details to student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Major details added to student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization major year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to add major details to student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    ResponseEntity<Response> addMajorDetails(@PathVariable("studentId") Integer studentId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.addMajorDetails(studentId,  studentMajorDetailsRequest.specializationMajorYearId());
    }

    @PutMapping("/{studentId}/major-details/{studentMajorDetailsId}")
    @Operation(summary = "Edit student major details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Student major details edited", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization major year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Major details not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to edit student major details", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    ResponseEntity<Response> editMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("studentMajorDetailsId") Integer studentMajorDetailsId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.editMajorDetails(studentId, studentMajorDetailsId, studentMajorDetailsRequest.specializationMajorYearId());
    }

    @DeleteMapping("/{studentId}/major-details/{majorDetailsId}")
    @Operation(summary = "Delete major details from student",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Major details deleted from student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization major year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete major details from student", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    ResponseEntity<Response> deleteMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("majorDetailsId") Integer majorDetailsId) {
        return studentService.deleteMajorDetails(studentId, majorDetailsId);
    }

}
