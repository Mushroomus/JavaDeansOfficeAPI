package com.example.deansoffice.controller;

import com.example.deansoffice.componentaspect.AdminLoggingAspect;
import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "API for administrative actions")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    private AdminService adminService;
    private AdminLoggingAspect adminLoggingAspect;

    AdminController(@Qualifier("adminServiceImpl") AdminService theAdminService, AdminLoggingAspect theAdminLoggingAspect) {
        adminService = theAdminService;
        adminLoggingAspect = theAdminLoggingAspect;
    }


    @GetMapping("/{adminId}/worker")
    public List<WorkerDTO> getWorkers(@PathVariable("adminId") int adminId) {
        return adminService.getWorkers();
    }

    // add logger to mark what admin done
    @PostMapping("/{adminId}/worker")
    @Operation(summary = "Create new Worker",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Worker created",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Worker saved\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to save",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to save\"\n}"))),
            })
    public ResponseEntity<Response> createWorker(@PathVariable("adminId") int adminId, @RequestBody WorkerDTO workerDTO) throws MessagingException {
        return adminService.createWorker(WorkerDTO.toEntity(workerDTO));
    }

    @PostMapping("/{adminId}/worker/{workerId}/specialization")
    @Operation(summary = "Add specializations to Worker",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specializations added to worker",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specializations added\"\n}"))),
                    @ApiResponse(responseCode = "400", description = "Worker already has these specializations",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Worker already has all the required specializations\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Worker not found",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Worker already has all the required specializations\"\n}"))),
            })
    public ResponseEntity<Response> addSpecializationsToWorker(@PathVariable("adminId") int adminId, @PathVariable("workerId") int workerId, @RequestBody List<Integer> specializationsIdList) {
        return adminService.addSpecializationsToWorker(workerId, specializationsIdList);
    }

    @DeleteMapping("/{adminId}/worker/{workerId}/specialization/{specializationId}")
    @Operation(summary = "Delete a specialization from a worker",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization deleted",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specialization deleted\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Worker was not found",
                            content = @Content(schema = @Schema(implementation = Map.class)))
            })
    public ResponseEntity<Response> deleteSpecializationFromWorker(@PathVariable("adminId") @Parameter(description="Admin Identificator") int adminId, @PathVariable("workerId") int workerId, @PathVariable("specializationId") int specializationId ) {
        return adminService.deleteSpecializationFromWorker(workerId, specializationId);
    }


    @GetMapping(value = "/{adminId}/major-year")
    public List<MajorYearDTO> getMajorYears(@PathVariable("adminId") Integer adminId) {
        return adminService.getMajorYears();
    }

    @PostMapping("/{adminId}/major-year")
    @Operation(summary = "Add new Major Year",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Major Year added",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Year added\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to add new Major Year",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to add year\"\n}"))),
            })
    public ResponseEntity<Response> addMajorYear(@PathVariable("adminId") Integer adminId, @RequestBody MajorYearDTO majorYearDTO) {
        return adminService.addMajorYear(MajorYearDTO.toEntity(majorYearDTO));
    }

    @DeleteMapping("/{adminId}/major-year/{yearId}")
    @Operation(summary = "Delete Major Year",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Major Year deleted",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Major Year deleted\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Major year doesn't exist with that ID",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Major Year doesn't exist\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to delete Major Year",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to delete year\"\n}"))),
            })
    public ResponseEntity<Response> deleteMajorYear(@PathVariable("adminId") Integer adminId, @PathVariable("yearId") Integer yearId) {
        return adminService.deleteMajorYear(yearId);
    }

    @GetMapping(value = "/{adminId}/specialization")
    public List<SpecializationDTO> getSpecializations(@PathVariable("adminId") Integer adminId) {
        return adminService.getSpecializations();
    }

    @PostMapping("/{adminId}/specialization")
    @Operation(summary = "Add new Specialization",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Specialization added",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specialization added\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to delete Specialization",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to save specialization\"\n}"))),
            })
    public ResponseEntity<Response> addSpecialization(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationDTO specializationDTO) {
        return adminService.addSpecialization(SpecializationDTO.toEntity(specializationDTO));
    }

    @PutMapping("/{adminId}/specialization")
    @Operation(summary = "Update Specializaion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization updated",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Major Year deleted\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Specialization year doesn't exist with that ID",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specialization not found\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to update Specialization",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Failed to update specialization\"\n}"))),
            })
    public ResponseEntity<Response> updateSpecialization(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationDTO specializationDTO) {
        return adminService.updateSpecialization(SpecializationDTO.toEntity(specializationDTO));
    }

    @DeleteMapping("/{adminId}/specialization/{specializationId}")
    @Operation(summary = "Delete Specializaion",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization deleted",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Major Year deleted\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Specialization doesn't exist with that ID",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Specialization not found\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to delete specialization",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Failed to delete specialization\"\n}"))),
            })
    public ResponseEntity<Response> deledeSpecialization(@PathVariable("adminId") Integer adminId, @PathVariable("specializationId") Integer specializationId) {
        return adminService.deleteSpecialization(specializationId);
    }


    @GetMapping(value = "/{adminId}/specialization-major-year")
    public List<SpecializationMajorYearDTO> getSpecializationMajorYear(@PathVariable("adminId") Integer adminId) {
        return adminService.getSpecializationMajorYear();
    }

    @PostMapping("/{adminId}/specialization-major-year")
    @Operation(summary = "Add Specialization Major Year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization major year added",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specialization major year added\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Specialization doesn't exist with that ID",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Specialization not found\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Year doesn't exist with that ID",
                            content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = "{\n  \"message\": \"Year not found\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to delete specialization",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Failed to add specialization major year\"\n}"))),
            })
    public ResponseEntity<Response> addSpecializationMajorYear(@PathVariable("adminId") Integer adminId, @RequestBody SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminService.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }

    @DeleteMapping("/{adminId}/specialization-major-year/{specializationMajorYearId}")
    @Operation(summary = "Delete Specializaion Major Year",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialization Major Year deleted",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Specialization Major Year deleted\"\n}"))),
                    @ApiResponse(responseCode = "404", description = "Specialization with that year doesn't exist",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Specialization with that year doesn't exist\"\n}"))),
                    @ApiResponse(responseCode = "500", description = "Failed to delete Specialization Major Year",
                            content = @Content(schema = @Schema(implementation = Map.class),
                                    examples = @ExampleObject(value = "{\n  \"message\": \"Failed to delete specialization major year\"\n}"))),
            })
    public ResponseEntity<Response> deleteSpecializationMajorYear(@PathVariable("adminId") Integer adminId, @PathVariable("specializationMajorYearId") Integer specializationMajorYearId) {
        return adminService.deleteSpecializationMajorYear(specializationMajorYearId);
    }
}
