package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.WorkerMapper;
import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.dao.SpecializationMajorYearDAO;
import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.exception.SpecializationNotFoundException;
import com.example.deansoffice.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerServiceImpl implements WorkerService {
    private WorkerDAO workerDAO;
    private SpecializationDAO specializationDAO;

    private WorkDateDAO workDateDAO;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    public WorkerServiceImpl(WorkerDAO theWorkerDAO, WorkDateDAO theWorkDateDAO, SpecializationDAO theSpecializationDAO) {
        workerDAO = theWorkerDAO;
        specializationDAO = theSpecializationDAO;
        workDateDAO = theWorkDateDAO;
    }

    public List<WorkerDTO> getWorkers() {
        return workerMapper.toDTOList(workerDAO.findAll());
    }

    public Optional<Worker> getWorkerById(int id) {
        return workerDAO.findById(id);
    }

    @Override
    public void saveWorker(Worker worker) {
        workerDAO.save(worker);
    }

    @Override
    public void addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList) {
        specializationsIdList.forEach(specializationId -> {
            Optional<Specialization> specialization = specializationDAO.findById(specializationId);

            if(specialization.isPresent()) {
                worker.getSpecializations().add(specialization.get());
            } else {
                throw new SpecializationNotFoundException();
            }
        });
        workerDAO.save(worker);
    }
}
