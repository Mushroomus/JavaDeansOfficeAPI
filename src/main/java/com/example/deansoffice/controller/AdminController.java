package com.example.deansoffice.controller;

import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.*;
import com.example.deansoffice.service.LoginAuthenticationJWT.LoginService;
import com.example.deansoffice.service.Manager.AdminMajorYearManager;
import com.example.deansoffice.service.Manager.AdminSpecializationMajorYearManager;
import com.example.deansoffice.service.Utils.PasswordGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private WorkerService workerService;
    private LoginService loginService;
    private PasswordGenerator passwordGenerator;

    private EmailService emailService;



    private AdminMajorYearManager adminMajorYearManager;
    private AdminSpecializationMajorYearManager adminSpecializationMajorYearManager;

    AdminController(@Qualifier("workerServiceImpl") WorkerService theWorkerService, @Qualifier("loginServiceImpl") LoginService theLoginService, AdminMajorYearManager theAdminMajorYearManager, AdminSpecializationMajorYearManager theAdminSpecializationMajorYearManager, PasswordGenerator thePasswordGenerator, EmailService theEmailService) {
        workerService = theWorkerService;
        loginService = theLoginService;
        passwordGenerator = thePasswordGenerator;
        emailService = theEmailService;
        adminMajorYearManager = theAdminMajorYearManager;
        adminSpecializationMajorYearManager = theAdminSpecializationMajorYearManager;
    }

    // add logger to mark what admin done
    @PostMapping("/{adminId}/worker")
    public ResponseEntity<Map<String,String>> createWorker(@PathVariable("adminId") int adminId, @RequestBody Worker newWorker) throws MessagingException {
        Worker worker = Worker.builder()
                .name(newWorker.getName())
                .surname(newWorker.getSurname())
                .phoneNumber(newWorker.getPhoneNumber())
                .email(newWorker.getEmail())
                .room(newWorker.getRoom())
                .build();
        workerService.saveWorker(worker);

        Map<String, String> response = new HashMap<>();

        if (worker.getId() > 0) {

            String generatedPassword = passwordGenerator.generateRandomPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(generatedPassword);


            Map<String, Object> model = new HashMap<>();
            model.put("workerNameAndSurname", worker.getName() + " " + worker.getSurname());
            model.put("password", generatedPassword);
            emailService.sendEmail(worker.getEmail(), "New worker account created", model, "worker-password");

            Login workerLogin = Login.builder()
                            .worker(worker)
                            .role(Role.WORKER)
                            .username(worker.getEmail())
                            .password(hashedPassword)
                            .build();

            loginService.saveLogin(workerLogin);

            response.put("message", "Worker saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("message", "Failed to save");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{adminId}/worker/{workerId}/specializations")
    public ResponseEntity<Map<String,String>> addSpecializationsToWorker(@PathVariable("adminId") int adminId, @PathVariable("workerId") int workerId, @RequestBody List<Integer> specializationsIdList) {
        Optional<Worker> worker = workerService.getWorkerById(workerId);
        Map<String, String> response = new HashMap<>();
        Worker workerChange;

        if(worker.isPresent()) {
            workerChange = worker.get();
        } else {
            response.put("message", "Worker not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            workerService.addNewSpecializationsToWorker(workerChange, specializationsIdList);
            response.put("message", "Specializations added successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{adminId}/major-year")
    public ResponseEntity<Map<String,String>> addMajorYear(@RequestBody MajorYear majorYear) {
        return adminMajorYearManager.addMajorYear(majorYear.getYear());
    }

    @DeleteMapping("/{adminId}/major-year/{yearId}")
    public ResponseEntity<Map<String,String>> deleteMajorYear(@PathVariable("yearId") Integer yearId) {
        return adminMajorYearManager.deleteMajorYear(yearId);
    }

    @PostMapping("/{adminId}/specialization-major-year")
    public ResponseEntity<Map<String,String>> addSpecializationMajorYear(@RequestBody SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminSpecializationMajorYearManager.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }

    @DeleteMapping("/{adminId}/specialization-major-year/{specializationMajorYearId}")
    public ResponseEntity<Map<String,String>> deleteSpecializationMajorYear(@PathVariable("specializationMajorYearId") Integer specializationMajorYearId) {
        return adminSpecializationMajorYearManager.deleteSpecializationMajorYear(specializationMajorYearId);
    }
}
