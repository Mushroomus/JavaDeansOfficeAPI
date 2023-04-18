package com.example.deansoffice.service.Manager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminWorkerSpecializationManager {
    ResponseEntity<Map<String,String>> deleteWorkerSpecialization(Integer workerId, Integer workerSpecializationId);
}
