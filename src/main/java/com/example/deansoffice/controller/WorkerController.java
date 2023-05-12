package com.example.deansoffice.controller;

import com.example.deansoffice.model.*;
import com.example.deansoffice.record.*;
import com.example.deansoffice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private WorkerService workerService;

    public WorkerController(@Qualifier("workerServiceImpl") WorkerService theWorkerService) {
        workerService = theWorkerService;
    }

    @DeleteMapping("/{workerId}/workdays")
    public ResponseEntity<Response> deleteListOfWorkDates(@PathVariable int workerId, @RequestBody IntegerListIdRequest workDatesList) {
        return workerService.deleteListOfWorkDates(workerId, workDatesList.listId());
    }

    @DeleteMapping("/{workerId}/workdays/{workdayId}")
    public ResponseEntity<Response> deleteWorkDate(@PathVariable int workerId, @PathVariable int workdayId) {
        return workerService.deleteSingleWorkDate(workerId, workdayId);
    }


    @DeleteMapping("/{workderId}/workdate-intervals")
    public ResponseEntity<Response> deleteListOfWorkDatesIntervals(@PathVariable int workerId, @RequestBody IntegerListIdRequest workDatesIntervalsList) {
        return workerService.deleteListOfWorkDatesIntervals(workerId, workDatesIntervalsList.listId());
    }

    @DeleteMapping("/{workderId}/workdate-intervals/{workdateIntervalId}")
    public ResponseEntity<Response> deleteSingleWorkDateInterval(@PathVariable int workerId, @PathVariable int workdateIntervalId) {
        return workerService.deleteSingleWorkDateInterval(workerId, workdateIntervalId);
    }

    @GetMapping("/{id}/workdays/{date}/intervals")
    public ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(@PathVariable int id, @PathVariable long date) {
        return workerService.getWorkDayIntervals(id, date);
    }

    // New workday with automatic created intervals
    @PostMapping("/{id}/workdays")
    public ResponseEntity<Response> newWorkDayForWorkerWithIntervals(@PathVariable int id, @RequestParam(defaultValue = "10") String interval, @RequestBody WorkerWorkDayPostRequest request) {
        return workerService.createNewWorkDayForWorkerWithIntervals(id, interval, request);
    }

    // New workday without intervals
    @PostMapping("/{id}/workdays/without-intervals")
    public ResponseEntity<Response> newWorkDayForWorkerWithoutIntervals(@PathVariable int id, @RequestBody WorkerWorkDayPostRequest request) {
        return workerService.createNewWorkDayForWorkerWithoutIntervals(id, request);
    }

    // Add custom intervals to workday
    @PostMapping("/{workerId}/workdays/{workDayId}/intervals")
    public ResponseEntity<Response> addIntervalsToWorkDay(@PathVariable int workerId, @PathVariable int workDayId, @RequestBody WorkerIntervalsToWorkDayPostRequest intervals) {
        return workerService.addIntervalsToWorkDay(workerId, workDayId, intervals);
    }

    @PutMapping("/{id}/workdays/{date}/interval/{time}/student/{student_id}")
    @Transactional
    public ResponseEntity<Response> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time, @PathVariable int student_id, @RequestBody(required = false) WorkerMakeAppointmentDescriptionPutRequest descriptionBody) {
        return workerService.makeAppointment(id, date, time, student_id, descriptionBody.description());
    }
}
