package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminSpecializationManager {
    ResponseEntity<Response> addSpecialization(Specialization specialization);

    ResponseEntity<Response> deleteSpecialization(Integer specializationId);

    ResponseEntity<Response> updateSpecialization(Specialization specialization);
}
