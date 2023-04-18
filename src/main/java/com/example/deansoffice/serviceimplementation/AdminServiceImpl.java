package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.componentaspect.AdminLoggingAspect;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.AdminService;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.Manager.*;
import com.example.deansoffice.service.Utils.PasswordGenerator;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private AdminMajorYearManager adminMajorYearManager;
    private AdminSpecializationManager adminSpecializationManager;
    private AdminSpecializationMajorYearManager adminSpecializationMajorYearManager;
    private AdminWorkerManager adminWorkerManager;
    private AdminLoginManager adminLoginManager;

    private AdminWorkerSpecializationManager adminWorkerSpecializationManager;
    private EmailService emailService;
    private PasswordGenerator passwordGenerator;

    AdminServiceImpl(AdminMajorYearManager theAdminMajorYearManager, AdminSpecializationManager theAdminSpecializationManager, AdminSpecializationMajorYearManager theAdminSpecializationMajorYearManager, AdminWorkerManager theAdminWorkerManager, AdminLoginManager theAdminLoginManager, AdminWorkerSpecializationManager theAdminWorkerSpecializationManager, EmailService theEmailService, PasswordGenerator thePasswordGenerator) {
        adminMajorYearManager = theAdminMajorYearManager;
        adminSpecializationManager = theAdminSpecializationManager;
        adminSpecializationMajorYearManager = theAdminSpecializationMajorYearManager;
        adminWorkerManager = theAdminWorkerManager;
        adminLoginManager = theAdminLoginManager;
        adminWorkerSpecializationManager = theAdminWorkerSpecializationManager;
        emailService = theEmailService;
        passwordGenerator = thePasswordGenerator;
    }

    public ResponseEntity<Map<String,String>> addMajorYear(MajorYear majorYear) {
        return adminMajorYearManager.addMajorYear(majorYear.getYear());
    }

    public ResponseEntity<Map<String,String>> deleteMajorYear(Integer yearId) {
        return adminMajorYearManager.deleteMajorYear(yearId);
    }

    public ResponseEntity<Map<String,String>> addSpecialization(Specialization specialization) {
        return adminSpecializationManager.addSpecialization(specialization);
    }

    public ResponseEntity<Map<String,String>> updateSpecialization(Specialization specialization) {
        return adminSpecializationManager.updateSpecialization(specialization);
    }

    public ResponseEntity<Map<String,String>> deleteSpecialization(Integer specializationId) {
        return adminSpecializationManager.deleteSpecialization(specializationId);
    }

    public ResponseEntity<Map<String,String>> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminSpecializationMajorYearManager.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }

    public ResponseEntity<Map<String,String>> deleteSpecializationMajorYear(Integer specializationMajorYearId) {
        return adminSpecializationMajorYearManager.deleteSpecializationMajorYear(specializationMajorYearId);
    }

    @Override
    public ResponseEntity<Map<String, String>> addSpecializationsToWorker(int workerId, List<Integer> specializationsIdList) {
        Optional<Worker> worker = adminWorkerManager.getWorkerById(workerId);
        Map<String, String> response = new HashMap<>();
        Worker workerChange;

        if(worker.isPresent()) {
            workerChange = worker.get();
        } else {
            response.put("message", "Worker not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        workerChange = adminWorkerManager.addNewSpecializationsToWorker(workerChange, specializationsIdList);
        List<Integer> workerSpecializationIds = workerChange.getSpecializations()
                .stream()
                .map(Specialization::getId)
                .toList();

        if (workerSpecializationIds.containsAll(specializationsIdList)) {
            response.put("message", "Worker already has all the required specializations");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            response.put("message", "Failed to add specializations");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> createWorker(Worker newWorker) throws MessagingException {
        Worker worker = Worker.builder()
                .name(newWorker.getName())
                .surname(newWorker.getSurname())
                .phoneNumber(newWorker.getPhoneNumber())
                .email(newWorker.getEmail())
                .room(newWorker.getRoom())
                .build();
        adminWorkerManager.saveWorker(worker);

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

            adminLoginManager.saveLogin(workerLogin);
            response.put("message", "Worker saved");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response.put("message", "Failed to save");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSpecializationFromWorker(int workerId, int workerSpecializationId) {
        Optional<Worker> worker = adminWorkerManager.getWorkerById(workerId);
        Map<String, String> response = new HashMap<>();
        if(worker.isEmpty()) {
            response.put("message", "Worker not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return adminWorkerSpecializationManager.deleteWorkerSpecialization(workerId, workerSpecializationId);
    }
}
