package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AdminWorkerManager {
    Optional<Worker> getWorkerById(int id);
    Worker addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList);
    void saveWorker(Worker worker);
}
