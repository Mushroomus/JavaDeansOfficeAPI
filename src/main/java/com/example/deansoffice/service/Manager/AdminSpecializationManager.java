package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Specialization;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminSpecializationManager {
    ResponseEntity<Map<String,String>> addSpecialization(Specialization specialization);

    ResponseEntity<Map<String, String>> deleteSpecialization(Integer specializationId);

    ResponseEntity<Map<String, String>> updateSpecialization(Specialization specialization);
}
