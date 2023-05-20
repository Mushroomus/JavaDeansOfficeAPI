package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.entity.WorkerSpecialization;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkerWorkerSpecializationManager {
    List<WorkerSpecialization> getWorkerSpecializationsByWorker(Integer workerId);
    ResponseEntity<Response> deleteWorkerSpecializationById(Integer workerSpecializationId);
    ResponseEntity<Response> addWorkerSpecialization(Worker worker, Specialization specialization);
}
