package com.example.deansoffice.controller;

import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.service.LoginService;
import com.example.deansoffice.service.PasswordGenerator;
import com.example.deansoffice.service.SpecializationMajorYearService;
import com.example.deansoffice.service.WorkerService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private WorkerService workerService;
    private LoginService loginService;
    private SpecializationMajorYearService specializationMajorYearService;
    private PasswordGenerator passwordGenerator;

    AdminController(@Qualifier("workerServiceImpl") WorkerService theWorkerService, @Qualifier("loginServiceImpl") LoginService theLoginService, @Qualifier("specializationMajorYearServiceImpl") SpecializationMajorYearService theSpecializationMajorYearService, PasswordGenerator thePasswordGenerator) {
        workerService = theWorkerService;
        loginService = theLoginService;
        specializationMajorYearService = theSpecializationMajorYearService;
        passwordGenerator = thePasswordGenerator;
    }

    // add logger to mark what admin done
    @PostMapping("/{adminId}/worker")
    public ResponseEntity<Map<String,String>> createWorker(@PathVariable("adminId") int adminId, @RequestBody Worker newWorker) {
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
            System.out.println(generatedPassword);

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
}
