package com.example.deansoffice.service;

import com.example.deansoffice.component.WorkerMapper;
import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerServiceImpl implements WorkerService {
    private WorkerDAO workerDAO;

    private WorkDateDAO workDateDAO;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    public WorkerServiceImpl(WorkerDAO theWorkerDAO, WorkDateDAO theWorkDateDAO) {
        workerDAO = theWorkerDAO;
        workDateDAO = theWorkDateDAO;
    }

    public List<WorkerDTO> getWorkers() {
        return workerMapper.toDTOList(workerDAO.findAll());
    }

    public Optional<Worker> getWorkerById(int id) {
        return workerDAO.findById(id);
    }

    public List<Integer> getWorkDaysForMonthAndYear(int workerId, int year, int month) {
        Optional<Worker> worker = getWorkerById(workerId);
        return worker.map(value -> workDateDAO.getDays(year, month, value)).orElse(null);
    }
}
