package com.example.deansoffice.controller;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.*;
import com.example.deansoffice.exception.IntervalNotFoundException;
import com.example.deansoffice.exception.StudentNotFoundException;
import com.example.deansoffice.exception.WorkDateNotFoundException;
import com.example.deansoffice.exception.WorkerNotFoundException;
import com.example.deansoffice.model.NewWorkDayRequest;
import com.example.deansoffice.model.Pair;
import com.example.deansoffice.service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private WorkerService workerService;

    public WorkerController(WorkerService theWorkerService) {
        workerService = theWorkerService;
    }

    @GetMapping("")
    public ResponseEntity<List<WorkerDTO>> getWorkers() {
        List<WorkerDTO> workersDTO = workerService.getWorkers();
        return ResponseEntity.ok(workersDTO);
    }

    @GetMapping("/{id}/workdays/{date}/intervals")
    public ResponseEntity<Map<String,List<String>>> getWorkDayIntervals(@PathVariable int id, @PathVariable long date) {

        List<String> intervals = workerService.getWorkDayIntervals(id, date);
        Map<String, List<String>> intervalsJson = new HashMap<>();
        intervalsJson.put("intervals", intervals);

        return ResponseEntity.ok().body(intervalsJson);
    }

    // New workday with automatic created intervals
    @PostMapping("/{id}/workdays")
    public ResponseEntity<String> newWorkDayForWorkerWithIntervals(@PathVariable int id, @RequestParam(defaultValue = "10") String interval, @RequestBody NewWorkDayRequest request) {
        workerService.createNewWorkDayForWorkerWithIntervals(id, interval, request);
        return ResponseEntity.ok("Work day saved");
    }

    // New workday without intervals
    @PostMapping("/{id}/workdays/without-intervals")
    public ResponseEntity<String> newWorkDayForWorkerWithoutIntervals(@PathVariable int id, @RequestBody NewWorkDayRequest request) {
        workerService.createNewWorkDayForWorkerWithoutIntervals(id, request);
        return ResponseEntity.ok("Work day saved");
    }

    // Add custom intervals to workday
    @PostMapping("/{workerId}/workdays/{workDayId}/intervals")
    public ResponseEntity<String> addIntervalsToWorkDay(@PathVariable int workerId, @PathVariable int workDayId, @RequestBody List<Pair<String,String>> intervals) {
        workerService.addIntervalsToWorkDay(workerId, workDayId, intervals);
        return ResponseEntity.ok("Intervals saved");
    }

    @PutMapping("/{id}/workdays/{date}/interval/{time}/student/{student_id}")
    @Transactional
    public ResponseEntity<String> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time, @PathVariable int student_id, @RequestBody(required = false) Map<String, String> descriptionBody) {
        workerService.makeAppointment(id, date, time, student_id, descriptionBody);
        return ResponseEntity.ok("Appointment made successfully.");
    }
}
