package com.example.deansoffice.service;

import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.SpecializationMajorYearPostRequest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {
    List<MajorYearDTO> getMajorYears();
    ResponseEntity<Response> addMajorYear(MajorYear majorYear);
    ResponseEntity<Response> deleteMajorYear(Integer yearId);
    List<SpecializationDTO> getSpecializations();
    ResponseEntity<Response> addSpecialization(Specialization specialization);
    ResponseEntity<Response> updateSpecialization(Specialization specialization);
    ResponseEntity<Response> deleteSpecialization(Integer specializationId);
    List<SpecializationMajorYearDTO> getSpecializationMajorYear();
    ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);
    ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId);
    ResponseEntity<Response> addSpecializationsToWorker(int workerId, List<Integer> specializationsIdList);
    List<WorkerDTO> getWorkers();
    ResponseEntity<Response> createWorker(Worker newWorker) throws MessagingException;
    ResponseEntity<Response> deleteSpecializationFromWorker(int workerId, int specializationId);
}
