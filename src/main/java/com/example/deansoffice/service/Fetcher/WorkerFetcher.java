package com.example.deansoffice.service.Fetcher;

import com.example.deansoffice.entity.Worker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface WorkerFetcher {
    Optional<Worker> getWorkerById(int id);
}
