package com.example.deansoffice.service.Manager;

import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminWorkerSpecializationManager {
    ResponseEntity<Response> deleteWorkerSpecialization(Integer workerId, Integer workerSpecializationId);
}
