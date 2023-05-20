package com.example.deansoffice.controller;

import com.example.deansoffice.componentaspect.AdminLoggingAspect;
import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.model.ErrorResponse;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints for administrative actions")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private AdminService adminService;
    private AdminLoggingAspect adminLoggingAspect;

    AdminController(@Qualifier("adminServiceImpl") AdminService theAdminService, AdminLoggingAspect theAdminLoggingAspect) {
        adminService = theAdminService;
        adminLoggingAspect = theAdminLoggingAspect;
    }


    @GetMapping("/{adminId}/workers")
    @Operation(summary = "Get workers", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = WorkerDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to get workers", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public List<WorkerDTO> getWorkers(@PathVariable("adminId") int adminId) {
        return adminService.getWorkers();
    }

    // add logger to mark what admin done
    @PostMapping("/{adminId}/worker")
    @Operation(summary = "Create worker", responses = {
            @ApiResponse(responseCode = "200", description = "Worker created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to send email", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to create worker", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Response> createWorker(@PathVariable("adminId") int adminId, @RequestBody WorkerDTO workerDTO) throws MessagingException {
        return adminService.createWorker(WorkerDTO.toEntity(workerDTO));
    }

    @PostMapping("/{adminId}/worker/{workerId}/specializations")
    @Operation(summary = "Add specializations to worker", responses = {
            @ApiResponse(responseCode = "200", description = "Specializations added to worker", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "400", description = "Worker already has these specializations", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Specialization not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to add specializations to worker", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Response> addSpecializationsToWorker(@PathVariable("adminId") int adminId, @PathVariable("workerId") int workerId, @RequestBody List<Integer> specializationsIdList) {
        return adminService.addSpecializationsToWorker(workerId, specializationsIdList);
    }

    @DeleteMapping("/{adminId}/worker/{workerId}/specialization/{specializationId}")
    @Operation(summary = "Delete specialization from worker", responses = {
            @ApiResponse(responseCode = "200", description = "Worker specialization deleted", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "404", description = "Worker not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Worker specialization not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to delete specialization from worker", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Response> deleteSpecializationFromWorker(@PathVariable("adminId") @Parameter(description="Admin Identificator") int adminId, @PathVariable("workerId") int workerId, @PathVariable("specializationId") int specializationId ) {
        return adminService.deleteSpecializationFromWorker(workerId, specializationId);
    }


    @GetMapping(value = "/{adminId}/major-years")
    @Operation(summary = "Get major years", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MajorYearDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to get major years", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public List<MajorYearDTO> getMajorYears(@PathVariable("adminId") Integer adminId) {
        return adminService.getMajorYears();
    }

    @PostMapping("/{adminId}/major-year")
    @Operation(summary = "Add major year", responses = {
            @ApiResponse(responseCode = "201", description = "Major year created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to create major year", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Response> addMajorYear(@PathVariable("adminId") Integer adminId, @RequestBody MajorYearDTO majorYearDTO) {
        return adminService.addMajorYear(MajorYearDTO.toEntity(majorYearDTO));
    }

    @DeleteMapping("/{adminId}/major-year/{yearId}")
    @Operation(summary = "Delete Major Year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Major year deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Major year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete major year", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteMajorYear(@PathVariable("adminId") Integer adminId, @PathVariable("yearId") Integer yearId) {
        return adminService.deleteMajorYear(yearId);
    }

    @GetMapping(value = "/{adminId}/specializations")
    @Operation(summary = "Get specializations", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpecializationDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to get specializations", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public List<SpecializationDTO> getSpecializations(@PathVariable("adminId") Integer adminId) {
        return adminService.getSpecializations();
    }

    @PostMapping("/{adminId}/specialization")
    @Operation(summary = "Add specialization", responses = {
            @ApiResponse(responseCode = "201", description = "Specialization created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to create specialization", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public ResponseEntity<Response> addSpecialization(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationDTO specializationDTO) {
        return adminService.addSpecialization(SpecializationDTO.toEntity(specializationDTO));
    }

    @PutMapping("/{adminId}/specialization")
    @Operation(summary = "Update specializaion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to update specialization", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> updateSpecialization(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationDTO specializationDTO) {
        return adminService.updateSpecialization(SpecializationDTO.toEntity(specializationDTO));
    }

    @DeleteMapping("/{adminId}/specialization/{specializationId}")
    @Operation(summary = "Delete specialization",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete specialization", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deledeSpecialization(@PathVariable("adminId") Integer adminId, @PathVariable("specializationId") Integer specializationId) {
        return adminService.deleteSpecialization(specializationId);
    }


    @GetMapping(value = "/{adminId}/specialization-major-years")
    @Operation(summary = "Get specialization major years", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = SpecializationMajorYearDTO.class))
            }),
            @ApiResponse(responseCode = "500", description = "Failed to get specialization major years", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    public List<SpecializationMajorYearDTO> getSpecializationMajorYear(@PathVariable("adminId") Integer adminId) {
        return adminService.getSpecializationMajorYear();
    }

    @PostMapping("/{adminId}/specialization-major-year")
    @Operation(summary = "Add Specialization Major Year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization major year added", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to add specialization major year", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> addSpecializationMajorYear(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminService.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }

    @DeleteMapping("/{adminId}/specialization-major-year/{specializationMajorYearId}")
    @Operation(summary = "Delete Specializaion Major Year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization major year deleted", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Specialization major year not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Failed to delete specialization major year", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            })
    public ResponseEntity<Response> deleteSpecializationMajorYear(@PathVariable("adminId") Integer adminId, @PathVariable("specializationMajorYearId") Integer specializationMajorYearId) {
        return adminService.deleteSpecializationMajorYear(specializationMajorYearId);
    }
}
