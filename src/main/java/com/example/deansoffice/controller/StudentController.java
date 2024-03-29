package com.example.deansoffice.controller;

import com.example.deansoffice.dto.*;
import com.example.deansoffice.model.ErrorResponse;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import com.example.deansoffice.record.StudentMajorDetailsPostAndPutRequest;
import com.example.deansoffice.record.WorkDayIntervalsGetResponse;
import com.example.deansoffice.record.MakeAppointmentDescriptionPutRequest;
import com.example.deansoffice.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
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

    private StudentService studentService;

    StudentController(@Qualifier("studentServiceImpl") StudentService theStudentService) {
        this.studentService = theStudentService;
    }

    @GetMapping("/{studentId}/major-details")
    @Operation(summary = "Get students",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SpecializationMajorYearDTO.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get specialization major years", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<SpecializationMajorYearDTO>> getStudentMajorDetails(@PathVariable("studentId") Integer studentId) {
        return studentService.getSpecializationMajorYear();
    }

    @GetMapping("/{studentId}/workers")
    @Operation(summary = "Get workers",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = WorkerDTO.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get workers", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public List<WorkerDTO> getWorkers(@PathVariable("studentId") Integer studentId, @RequestParam(value = "matchSpecializations", defaultValue = "false") String matchSpecializations) {
        return studentService.getWorkersWithOptionalMatch(studentId, !matchSpecializations.equals("false"));
    }

    @GetMapping("/{studentId}/worker/{workerId}/workdays")
    @Operation(summary = "Get worker workdays",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LocalDate.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get worker workdays", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<LocalDate>> getWorkDays(@PathVariable("studentId") Integer studentId, @PathVariable("workerId") Integer workerId) {
        return studentService.getWorkDays(workerId);
    }

    @GetMapping("/{studentId}/worker/{workerId}/workdays/{date}/intervals")
    @Operation(summary = "Get worker work day intervals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = WorkDayIntervalsGetResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Work daate not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Intervals not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get work day intervals", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(@PathVariable("studentId") Integer studentId, @PathVariable("workerId") Integer workerId,
                                                                           @PathVariable("date") long date) {
        return studentService.getWorkerWorkDayIntervals(workerId, date);
    }

    @PutMapping("{studentId}/worker/{workerId}/workdays/{date}/interval/{time}")
    @Transactional
    @Operation(summary = "Make an appointment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Student not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Work date not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Interval not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to make an appointment", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> makeAppointment(@PathVariable("studentId") Integer studentId, @PathVariable("workerId") Integer workerId,
                                                    @PathVariable long date, @PathVariable String time, @RequestBody(required = false) MakeAppointmentDescriptionPutRequest descriptionBody) {
        return studentService.makeAppointment(workerId, date, time, studentId, descriptionBody != null ? descriptionBody.description() : null);
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
    public StudentDTO getStudent(@PathVariable("studentId") Integer studentId) {
        return studentService.getStudent(studentId);
    }

    @GetMapping("/{studentId}/specialization-major-year")
    @Operation(summary = "Get specialization major years", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpecializationMajorYearDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to get specialization major years", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<List<SpecializationMajorYearDTO>> getSpecializationMajorYear(@PathVariable("studentId") Integer studentId) {
        return studentService.getSpecializationMajorYear();
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
    public ResponseEntity<Response> updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudent(studentId, StudentDTO.toEntity(studentDTO));
    }

    @GetMapping("/{studentId}/appointment/{appointmentId}")
    @Operation(summary = "Cancel student appointment",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Access forbidden", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Appointment not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to cancel appointment", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> cancelAppointment(@PathVariable int studentId, @PathVariable int appointmentId) {
        return studentService.cancelAppointment(studentId, appointmentId);
    }

    @GetMapping("/{studentId}/appointments")
    @Operation(summary = "Get student appointments",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = StudentAppointmentGetResponse.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Failed to parse date", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get student appointments", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<StudentAppointmentGetResponse>> getAppointments(@PathVariable int studentId,
                                                           @RequestParam(required = false) String startInterval, @RequestParam(required = false) String endInterval,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                           @RequestParam(required = false) Integer workerId) {
        return studentService.getAppointments(studentId, startInterval, endInterval, startDate, endDate, workerId);
    }

    @GetMapping("/{studentId}/own-major-details")
    @Operation(summary = "Get student major details",
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
    public ResponseEntity<List<SpecializationMajorYearDTO>> getStudentSpecializationMajorYear(@PathVariable("studentId") int studentId) {
        return studentService.getStudentSpecializationMajorYear(studentId);
    }

    @PostMapping("/{studentId}/own-major-details")
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
    public ResponseEntity<Response> addMajorDetails(@PathVariable("studentId") Integer studentId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.addMajorDetails(studentId,  studentMajorDetailsRequest.specializationMajorYearId());
    }

    /*
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
    public ResponseEntity<Response> editMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("studentMajorDetailsId") Integer studentMajorDetailsId, @RequestBody StudentMajorDetailsPostAndPutRequest studentMajorDetailsRequest) {
        return studentService.editMajorDetails(studentId, studentMajorDetailsId, studentMajorDetailsRequest.specializationMajorYearId());
    }
     */

    @DeleteMapping("/{studentId}/own-major-details/{majorDetailsId}")
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
    public ResponseEntity<Response> deleteMajorDetails(@PathVariable("studentId") Integer studentId, @PathVariable("majorDetailsId") Integer majorDetailsId) {
        return studentService.deleteMajorDetails(studentId, majorDetailsId);
    }
}
