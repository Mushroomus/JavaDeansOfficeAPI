package com.example.deansoffice.service;

import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.dto.WorkerSpecializationDTO;
import com.example.deansoffice.model.*;
import com.example.deansoffice.record.WorkDayIntervalsGetResponse;
import com.example.deansoffice.record.WorkerIntervalsToWorkDayPostRequest;
import com.example.deansoffice.record.WorkerWorkDayPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface WorkerService {
    List<WorkerDTO> getWorkers();
    ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(int id, long date);
    ResponseEntity<Response> createNewWorkDayForWorkerWithIntervals(int workerId, String interval, WorkerWorkDayPostRequest request);
    ResponseEntity<Response> createNewWorkDayForWorkerWithoutIntervals(int id, WorkerWorkDayPostRequest request);
    ResponseEntity<Response> addIntervalsToWorkDay(int workerId, int workDayId, WorkerIntervalsToWorkDayPostRequest intervals);
    ResponseEntity<List<SpecializationDTO>> getSpecializations();
    ResponseEntity<Response> makeAppointment(int id, long date, String time, int studentId, Map<String, String> descriptionBody);
    ResponseEntity<Response> deleteSingleWorkDate(Integer workerId, Integer workdayId);
    ResponseEntity<Response> deleteListOfWorkDates(Integer workerId, List<Integer> workDatesListId);
    ResponseEntity<Response> deleteSingleWorkDateInterval(Integer workerId, Integer workdateIntervalId);
    ResponseEntity<Response> deleteListOfWorkDatesIntervals(Integer workerId, List<Integer> workDatesIntervalsListId);
    ResponseEntity<List<LocalDate>> getWorkDays(Integer workerId);
    ResponseEntity<List<WorkerSpecializationDTO>> getWorkerSpecializations(Integer workerId);
    ResponseEntity<Response> deleteWorkerSpecializationById(Integer workerSpecializationId);
    ResponseEntity<Response> addWorkerSpecialization(int workerId, int specializationId);
    ResponseEntity<Response> cancelAppointment(Integer workerId, Integer appointmentId);
}
