package com.example.deansoffice.service;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.NewWorkDayRequest;
import com.example.deansoffice.model.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface WorkerService {
    List<WorkerDTO> getWorkers();
    Optional<Worker> getWorkerById(int id);

    void saveWorker(Worker worker);

    void addNewSpecializationsToWorker(Worker worker, List<Integer> specializationsIdList);

    List<String> getWorkDayIntervals(int id, long date);

    void createNewWorkDayForWorkerWithIntervals(int workerId, String interval, NewWorkDayRequest request);

    void createNewWorkDayForWorkerWithoutIntervals(int id, NewWorkDayRequest request);

    void addIntervalsToWorkDay(int workerId, int workDayId, List<Pair<String, String>> intervals);

    void makeAppointment(int id, long date, String time, int studentId, Map<String, String> descriptionBody);
}
