package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public interface WorkerWorkDateManager {
    ResponseEntity<Response> deleteSingleWorkDate(Integer workerId, Integer workdayId);
    ResponseEntity<Response> deleteListOfWorkDates(Integer workerId, List<Integer> workDatesListId);
    WorkDate newWorkDateForUser(Worker worker, LocalDate date, LocalTime startTime, LocalTime endTime);
    Optional<WorkDate> findWorkDateById(Integer workDateId);
}
