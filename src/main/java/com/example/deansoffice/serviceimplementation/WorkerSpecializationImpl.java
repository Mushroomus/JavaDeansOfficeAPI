package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkerSpecializationDAO;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.entity.WorkerSpecialization;
import com.example.deansoffice.exception.AccessForbiddenException;
import com.example.deansoffice.exception.BadRequestException;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.service.Manager.AdminWorkerSpecializationManager;
import com.example.deansoffice.service.Manager.WorkerWorkerSpecializationManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkerSpecializationImpl implements AdminWorkerSpecializationManager, WorkerWorkerSpecializationManager {
    private WorkerSpecializationDAO workerSpecializationDAO;
    public WorkerSpecializationImpl(WorkerSpecializationDAO theWorkerSpecializationDAO) {
        workerSpecializationDAO = theWorkerSpecializationDAO;
    }

    @Override
    public ResponseEntity<Response> deleteWorkerSpecialization(Integer workerId, Integer workerSpecializationId) {
        Optional<WorkerSpecialization> workerSpecialization = workerSpecializationDAO.findById(workerSpecializationId);

        if(workerSpecialization.isEmpty()) {
            throw new RecordNotFoundException("Worker specialization not found");
        }

        WorkerSpecialization deleteWorkerSpecialization = workerSpecialization.get();

        if(deleteWorkerSpecialization.getWorker().getId() != workerId) {
            throw new AccessForbiddenException();
        }

        deleteWorkerSpecialization.setWorker(null);
        deleteWorkerSpecialization.setSpecialization(null);
        workerSpecializationDAO.delete(deleteWorkerSpecialization);

        if(workerSpecializationDAO.findById(workerSpecializationId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Worker specialization deleted"));
        } else {
            throw new InternalServerErrorException("Failed to delete specialization from worker");
        }
    }

    @Override
    public ResponseEntity<Response> deleteWorkerSpecializationById(Integer workerSpecializationId) {
        try {
            workerSpecializationDAO.deleteById(workerSpecializationId);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Worker specialization deleted"));
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete worker specialization");
        }
    }

    @Override
    public ResponseEntity<Response> addWorkerSpecialization(Worker worker, Specialization specialization) {
        try {
            if (workerSpecializationDAO.existsByWorkerAndSpecialization(worker, specialization)) {
                WorkerSpecialization addWorkerSpecialization = WorkerSpecialization
                        .builder()
                        .worker(worker)
                        .specialization(specialization)
                        .build();
                workerSpecializationDAO.save(addWorkerSpecialization);
                return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Specialization added to worker"));
            } else {
                throw new BadRequestException("Worker already has that specialization");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to add specialization to worker");
        }
    }

    @Override
    public List<WorkerSpecialization> getWorkerSpecializationsByWorker(Integer workerId) {
        return workerSpecializationDAO.findAllByWorkerId(workerId);
    }
}
