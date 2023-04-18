package com.example.deansoffice.controller;

import com.example.deansoffice.componentaspect.AdminLoggingAspect;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.*;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private AdminService adminService;
    private AdminLoggingAspect adminLoggingAspect;

    AdminController(AdminService theAdminService, AdminLoggingAspect theAdminLoggingAspect) {
        adminService = theAdminService;
        adminLoggingAspect = theAdminLoggingAspect;
    }

    // add logger to mark what admin done
    @PostMapping("/{adminId}/worker")
    public ResponseEntity<Map<String,String>> createWorker(@PathVariable("adminId") int adminId, @RequestBody Worker newWorker) throws MessagingException {
        return adminService.createWorker(newWorker);
    }

    @PostMapping("/{adminId}/worker/{workerId}/specialization")
    public ResponseEntity<Map<String,String>> addSpecializationsToWorker(@PathVariable("adminId") int adminId, @PathVariable("workerId") int workerId, @RequestBody List<Integer> specializationsIdList) {
        return adminService.addSpecializationsToWorker(workerId, specializationsIdList);
    }

    @DeleteMapping("/{adminId}/worker/{workerId}/specialization/{specializationId}")
    public ResponseEntity<Map<String,String>> deleteSpecializationFromWorker(@PathVariable("adminId") int adminId, @PathVariable("workerId") int workerId, @PathVariable("specializationId") int specializationId ) {
        return adminService.deleteSpecializationFromWorker(workerId, specializationId);
    }

    @PostMapping("/{adminId}/major-year")
    public ResponseEntity<Map<String,String>> addMajorYear(@RequestBody MajorYear majorYear) {
        return adminService.addMajorYear(majorYear);
    }

    @DeleteMapping("/{adminId}/major-year/{yearId}")
    public ResponseEntity<Map<String,String>> deleteMajorYear(@PathVariable("yearId") Integer yearId) {
        return adminService.deleteMajorYear(yearId);
    }

    @PostMapping("/{adminId}/specialization")
    public ResponseEntity<Map<String,String>> addSpecialization(@RequestBody Specialization specialization) {
        return adminService.addSpecialization(specialization);
    }

    @PutMapping("/{adminId}/specialization")
    public ResponseEntity<Map<String,String>> updateSpecialization(@RequestBody Specialization specialization) {
        return adminService.updateSpecialization(specialization);
    }

    @DeleteMapping("/{adminId}/specialization/{specializationId}")
    public ResponseEntity<Map<String,String>> deledeSpecialization(@PathVariable("specializationId") Integer specializationId) {
        return adminService.deleteSpecialization(specializationId);
    }

    @PostMapping("/{adminId}/specialization-major-year")
    public ResponseEntity<Map<String,String>> addSpecializationMajorYear(@RequestBody SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminService.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }

    @DeleteMapping("/{adminId}/specialization-major-year/{specializationMajorYearId}")
    public ResponseEntity<Map<String,String>> deleteSpecializationMajorYear(@PathVariable("specializationMajorYearId") Integer specializationMajorYearId) {
        return adminService.deleteSpecializationMajorYear(specializationMajorYearId);
    }
}
