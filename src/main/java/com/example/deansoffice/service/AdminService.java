package com.example.deansoffice.service;

import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminService {
    ResponseEntity<Map<String,String>> addMajorYear(MajorYear majorYear);
    ResponseEntity<Map<String,String>> deleteMajorYear(Integer yearId);
    ResponseEntity<Map<String,String>> addSpecialization(Specialization specialization);
    ResponseEntity<Map<String,String>> updateSpecialization(Specialization specialization);
    ResponseEntity<Map<String,String>> deleteSpecialization(Integer specializationId);
    ResponseEntity<Map<String,String>> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);
    ResponseEntity<Map<String,String>> deleteSpecializationMajorYear(Integer specializationMajorYearId);
    ResponseEntity<Map<String, String>> addSpecializationsToWorker(int workerId, List<Integer> specializationsIdList);
    ResponseEntity<Map<String, String>> createWorker(Worker newWorker) throws MessagingException;
    ResponseEntity<Map<String, String>> deleteSpecializationFromWorker(int workerId, int specializationId);
}
