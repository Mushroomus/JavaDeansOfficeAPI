package com.example.deansoffice.service;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.NewWorkDayRequest;
import com.example.deansoffice.model.Pair;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface WorkerService {
    List<WorkerDTO> getWorkers();

    List<String> getWorkDayIntervals(int id, long date);

    void createNewWorkDayForWorkerWithIntervals(int workerId, String interval, NewWorkDayRequest request);

    void createNewWorkDayForWorkerWithoutIntervals(int id, NewWorkDayRequest request);

    void addIntervalsToWorkDay(int workerId, int workDayId, List<Pair<String, String>> intervals);

    void makeAppointment(int id, long date, String time, int studentId, Map<String, String> descriptionBody);

    ResponseEntity<Response> deleteSingleWorkDate(Integer workerId, Integer workdayId);
    ResponseEntity<Response> deleteListOfWorkDates(Integer workerId, List<Integer> workDatesListId);
    ResponseEntity<Response> deleteSingleWorkDateInterval(Integer workerId, Integer workdateIntervalId);
    ResponseEntity<Response> deleteListOfWorkDatesIntervals(Integer workerId, List<Integer> workDatesIntervalsListId);

}
