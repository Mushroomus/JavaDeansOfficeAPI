package com.example.deansoffice.service;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface WorkerService {
    List<WorkerDTO> getWorkers();
    Optional<Worker> getWorkerById(int id);

    void saveWorker(Worker worker);

    void addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList);
}
