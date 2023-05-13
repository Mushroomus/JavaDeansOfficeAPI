package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.exception.BadRequestException;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.record.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.AdminService;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.Manager.*;
import com.example.deansoffice.service.Utils.PasswordGenerator;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Override
    public List<MajorYearDTO> getMajorYears() {
        return adminMajorYearManager.getMajorYears();
    }
    @Override
    public ResponseEntity<Response> addMajorYear(MajorYear majorYear) {
        return adminMajorYearManager.addMajorYear(majorYear.getYear());
    }
    @Override
    public ResponseEntity<Response> deleteMajorYear(Integer yearId) {
        return adminMajorYearManager.deleteMajorYear(yearId);
    }

    @Override
    public List<SpecializationDTO> getSpecializations() {
        return adminSpecializationManager.getSpecializations();
    }

    @Override
    public ResponseEntity<Response> addSpecialization(Specialization specialization) {
        return adminSpecializationManager.addSpecialization(specialization);
    }
    @Override
    public ResponseEntity<Response> updateSpecialization(Specialization specialization) {
        return adminSpecializationManager.updateSpecialization(specialization);
    }
    @Override
    public ResponseEntity<Response> deleteSpecialization(Integer specializationId) {
        return adminSpecializationManager.deleteSpecialization(specializationId);
    }

    @Override
    public List<SpecializationMajorYearDTO> getSpecializationMajorYear() {
        return adminSpecializationMajorYearManager.getSpecializationMajorYear();
    }

    @Override
    public ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        return adminSpecializationMajorYearManager.addSpecializationMajorYear(specializationMajorYearPostRequest);
    }
    @Override
    public ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId) {
        return adminSpecializationMajorYearManager.deleteSpecializationMajorYear(specializationMajorYearId);
    }


    @Override
    public ResponseEntity<Response> addSpecializationsToWorker(int workerId, List<Integer> specializationsIdList) {
        Optional<Worker> worker = adminWorkerManager.getWorkerById(workerId);
        Map<String, String> response = new HashMap<>();

        Worker workerChange = worker.orElseThrow(() -> new RecordNotFoundException("Worker not found"));

        workerChange = adminWorkerManager.addNewSpecializationsToWorker(workerChange, specializationsIdList);
        List<Integer> workerSpecializationIds = workerChange.getSpecializations()
                .stream()
                .map(Specialization::getId)
                .toList();

        if (new HashSet<>(workerSpecializationIds).containsAll(specializationsIdList)) {
            throw new BadRequestException("Worker already has all the required specializations");
        } else {
            response.put("message", "Specializations added");
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Specializations added to worker"));
        }
    }

    @Override
    public List<WorkerDTO> getWorkers() {
        return adminWorkerManager.getWorkers();
    }

    @Override
    public ResponseEntity<Response> createWorker(Worker newWorker) throws MessagingException {
        try {
            Worker worker = Worker.builder()
                    .name(newWorker.getName())
                    .surname(newWorker.getSurname())
                    .phoneNumber(newWorker.getPhoneNumber())
                    .email(newWorker.getEmail())
                    .room(newWorker.getRoom())
                    .build();
            adminWorkerManager.saveWorker(worker);

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
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Worker created"));
        } catch (MessagingException e) {
            throw new InternalServerErrorException("Failed to send email");
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to create worker");
        }
    }

    @Override
    public ResponseEntity<Response> deleteSpecializationFromWorker(int workerId, int workerSpecializationId) {
        try {
            Optional<Worker> worker = adminWorkerManager.getWorkerById(workerId);

            if (worker.isEmpty()) {
                throw new RecordNotFoundException("Worker not found");
            }
            return adminWorkerSpecializationManager.deleteWorkerSpecialization(workerId, workerSpecializationId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete specialization from worker");
        }
    }
}
