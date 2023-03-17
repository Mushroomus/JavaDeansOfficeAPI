package com.example.deansoffice.service;

import com.example.deansoffice.dao.WorkerDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerServiceImpl implements WorkerService {
    private WorkerDAO workerDAO;

    @Autowired
    public WorkerServiceImpl(WorkerDAO theWorkerDAO) {
        workerDAO = theWorkerDAO;
    }
}
