package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkerSpecializationDAO;
import com.example.deansoffice.entity.WorkerSpecialization;
import com.example.deansoffice.service.Manager.AdminWorkerSpecializationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkerSpecializationImpl implements AdminWorkerSpecializationManager {
    private WorkerSpecializationDAO workerSpecializationDAO;
    public WorkerSpecializationImpl(WorkerSpecializationDAO theWorkerSpecializationDAO) {
        workerSpecializationDAO = theWorkerSpecializationDAO;
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteWorkerSpecialization(Integer workerId, Integer workerSpecializationId) {
        Optional<WorkerSpecialization> workerSpecialization = workerSpecializationDAO.findById(workerSpecializationId);
        Map<String,String> response = new HashMap<>();

        if(workerSpecialization.isEmpty()) {
            response.put("response", "Worker Specialization not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        WorkerSpecialization deleteWorkerSpecialization = workerSpecialization.get();

        if(deleteWorkerSpecialization.getWorker().getId() != workerId) {
            response.put("response", "Access forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        deleteWorkerSpecialization.setWorker(null);
        deleteWorkerSpecialization.setSpecialization(null);
        workerSpecializationDAO.delete(deleteWorkerSpecialization);

        if(workerSpecializationDAO.findById(workerSpecializationId).isEmpty()) {
            response.put("response", "Worker Specialization deleted");
            return ResponseEntity.ok(response);
        } else {
            response.put("response", "Failed to delete Worker Specialization");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
