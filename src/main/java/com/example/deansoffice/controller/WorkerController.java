package com.example.deansoffice.controller;

import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.dto.WorkerSpecializationDTO;
import com.example.deansoffice.model.*;
import com.example.deansoffice.record.*;
import com.example.deansoffice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/worker")
@Tag(name = "Worker", description = "Endpoints for worker actions")
@SecurityRequirement(name = "bearerAuth")
public class WorkerController {
    private final WorkerService workerService;

    public WorkerController(@Qualifier("workerServiceImpl") WorkerService theWorkerService) {
        workerService = theWorkerService;
    }

    @GetMapping("/{workerId}/students")
    @Operation(summary = "Get students",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WorkerDTO.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get students", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<StudentDTO>> getStudents() {
        return workerService.getStudents();
    }


    @DeleteMapping("/{workerId}/workdays")
    @Operation(summary = "Delete worker work dates",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Worker work days deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "List of work dates is empty", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete worker work dates", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteListOfWorkDates(@PathVariable int workerId, @RequestBody IntegerListIdRequest workDatesList) {
        return workerService.deleteListOfWorkDates(workerId, workDatesList.listId());
    }

    @DeleteMapping("/{workerId}/workday/{workdayId}")
    @Operation(summary = "Delete worker work date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Worker work day deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete worker work date", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteWorkDate(@PathVariable int workerId, @PathVariable int workdayId) {
        return workerService.deleteSingleWorkDate(workerId, workdayId);
    }

    @DeleteMapping("/{workerId}/workdate-intervals")
    @Operation(summary = "Delete worker work date intervals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Worker work day intervals deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Work date interval list is empty", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete worker work date intervals", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteListOfWorkDatesIntervals(@PathVariable int workerId, @RequestBody IntegerListIdRequest workDatesIntervalsList) {
        return workerService.deleteListOfWorkDatesIntervals(workerId, workDatesIntervalsList.listId());
    }

    @DeleteMapping("/{workerId}/workdate-interval/{workdateIntervalId}")
    @Operation(summary = "Delete worker work date interval",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Worker work day interval deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete worker work date interval", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteSingleWorkDateInterval(@PathVariable int workerId, @PathVariable int workdateIntervalId) {
        return workerService.deleteSingleWorkDateInterval(workerId, workdateIntervalId);
    }

    @GetMapping("/{id}/workdays/{date}/intervals")
    @Operation(summary = "Get worker work date intervals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = WorkDayIntervalsGetResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Work date not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Interval not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get work day intervals", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(@PathVariable int id, @PathVariable long date) {
        return workerService.getWorkDayIntervals(id, date);
    }

    // New workday with automatic created intervals
    @PostMapping("/{id}/workdays")
    @Operation(summary = "Create work work day with automatic intervals",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Worker work day with intervals created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to create worker work day with intervals", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> newWorkDayForWorkerWithIntervals(@PathVariable int id, @RequestParam(defaultValue = "10") String interval, @RequestBody WorkerWorkDayPostRequest request) {
        return workerService.createNewWorkDayForWorkerWithIntervals(id, interval, request);
    }

    // New workday without intervals
    @PostMapping("/{id}/workdays/without-intervals")
    @Operation(summary = "Create work work day without intervals",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Worker work day without intervals created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to create worker work day without intervals", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> newWorkDayForWorkerWithoutIntervals(@PathVariable int id, @RequestBody WorkerWorkDayPostRequest request) {
        return workerService.createNewWorkDayForWorkerWithoutIntervals(id, request);
    }

    // Add custom intervals to workday
    @PostMapping("/{workerId}/workdays/{workDayId}/intervals")
    @Operation(summary = "Add custom worker work date intervals",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Intervals added to worker work day", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "New interval overlaps with existing interval", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "New intervals must be within the working hours of the start and end time", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Work date not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to add interval to worker work day", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> addIntervalsToWorkDay(@PathVariable int workerId, @PathVariable int workDayId, @RequestBody WorkerIntervalsToWorkDayPostRequest intervals) {
        return workerService.addIntervalsToWorkDay(workerId, workDayId, intervals);
    }

    @PutMapping("/{id}/workdays/{date}/interval/{time}/student/{student_id}")
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
    public ResponseEntity<Response> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time, @PathVariable int student_id, @RequestBody(required = false) MakeAppointmentDescriptionPutRequest descriptionBody) {
        return workerService.makeAppointment(id, date, time, student_id, descriptionBody != null ? descriptionBody.description() : null);
    }

    @GetMapping("/{workerId}/appointment/{appointmentId}")
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
    public ResponseEntity<Response> cancelAppointment(@PathVariable("workerId") Integer workerId, @PathVariable("appointmentId") Integer appointmentId) {
        return workerService.cancelAppointment(workerId, appointmentId);
    }

    @GetMapping("/{workerId}/workdays")
    @Operation(summary = "Get worker work days",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LocalDate.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get worker work dates", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<LocalDate>> getWorkDays(@PathVariable("workerId") Integer workerId) {
        return workerService.getWorkDays(workerId);
    }

    @GetMapping("/{workerId}/own-specializations")
    @Operation(summary = "Get worker specializations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WorkerSpecializationDTO.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get worker specializations", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<WorkerSpecializationDTO>> getWorkerSpecializations(@PathVariable("workerId") Integer workerId) {
        return workerService.getWorkerSpecializations(workerId);
    }

    @GetMapping("/{workerId}/specializations")
    @Operation(summary = "Get specializations",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SpecializationDTO.class)))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to get specializations", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<List<SpecializationDTO>> getSpecializations(@PathVariable("workerId") Integer workerId) {
        return workerService.getSpecializations();
    }

    @DeleteMapping("/{workerId}/own-specialization/{workerSpecializationId}")
    @Operation(summary = "Delete worker specialization",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker specialization not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete worker specialization", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteWorkerSpecialization(@PathVariable("workerId") Integer workerId, @PathVariable("workerSpecializationId") Integer workerSpecializationId) {
        return workerService.deleteWorkerSpecializationById(workerSpecializationId);
    }

    @PostMapping("/{workerId}/own-specialization/{specializationId}")
    @Operation(summary = "Add specialization to worker",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Worker already has that specialization", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to add specialization to worker", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> addWorkerSpecialization(@PathVariable int workerId, @PathVariable int specializationId) {
        return workerService.addWorkerSpecialization(workerId, specializationId);
    }

}
