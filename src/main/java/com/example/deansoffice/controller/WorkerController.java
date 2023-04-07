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
    private WorkDateService workDateService;
    private WorkDateIntervalsService workDateIntervalsService;
    private StudentService studentService;

    public WorkerController(WorkerService theWorkerService, WorkDateService theWorkDateService, WorkDateIntervalsService theWorkDateIntervalsService, StudentService theStudentService) {
        workerService = theWorkerService;
        workDateService = theWorkDateService;
        workDateIntervalsService = theWorkDateIntervalsService;
        studentService = theStudentService;
    }

    @GetMapping("")
    public ResponseEntity<List<WorkerDTO>> getWorkers() {
        List<WorkerDTO> workersDTO = workerService.getWorkers();
        return ResponseEntity.ok(workersDTO);
    }

    @GetMapping("/{id}/workdays/{date}/intervals")
    public ResponseEntity<Map<String,List<String>>> getWorkDayIntervals(@PathVariable int id, @PathVariable long date) {
        Optional<Worker> worker = workerService.getWorkerById(id);

        if(worker.isPresent()) {

            LocalDate convertedDate = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Optional<WorkDate> workDate = worker.get().getWorkDates().stream()
                    .filter(d -> d.getDate().equals(convertedDate))
                    .findFirst();

            if(workDate.isPresent()) {
                List<WorkDateIntervals> workDateIntervalsList = workDate.get().getWorkDateIntervals();

                if(workDateIntervalsList != null) {
                    List<String> intervals = workDateIntervalsList.stream().filter(w -> !w.getTaken()).map(w -> w.getStartInterval().toString() + " - " + w.getEndInterval().toString()).collect(Collectors.toList());
                    Map<String, List<String>> intervalsJson = new HashMap<>();
                    intervalsJson.put("intervals", intervals);

                    return ResponseEntity.ok().body(intervalsJson);
                } else {
                    throw new IntervalNotFoundException();
                }
            } else {
                throw new WorkDateNotFoundException();
            }
        } else {
            throw new WorkerNotFoundException();
        }
    }

    // New workday with automatic created intervals
    @PostMapping("/{id}/workdays")
    public ResponseEntity<String> newWorkDayForWorkerWithIntervals(@PathVariable int id, @RequestParam(defaultValue = "10") String interval, @RequestBody NewWorkDayRequest request) {
        Optional<Worker> worker = workerService.getWorkerById(id);

        if(worker.isPresent()) {
            WorkDate workDate = workDateService.newWorkDateForUser(worker.get(), request.getDate(), request.getStartTime(), request.getEndTime());
            workDateIntervalsService.createIntervals(workDate, Integer.parseInt(interval));
            return ResponseEntity.ok("Work day saved");
        } else {
            throw new WorkerNotFoundException();
        }
    }

    // New workday without intervals
    @PostMapping("/{id}/workdays/without-intervals")
    public ResponseEntity<String> newWorkDayForWorkerWithoutIntervals(@PathVariable int id, @RequestBody NewWorkDayRequest request) {
        Optional<Worker> worker = workerService.getWorkerById(id);

        if(worker.isPresent()) {
            workDateService.newWorkDateForUser(worker.get(), request.getDate(), request.getStartTime(), request.getEndTime());
            return ResponseEntity.ok("Work day saved");
        } else {
            throw new WorkerNotFoundException();
        }
    }

    // Add custom intervals to workday
    @PostMapping("/{workerId}/workdays/{workDayId}/intervals")
    public ResponseEntity<String> addIntervalsToWorkDay(@PathVariable int workerId, @PathVariable int workDayId, @RequestBody List<Pair<String,String>> intervals) {
        Optional<Worker> worker = workerService.getWorkerById(workerId);

        if(worker.isPresent()) {
            Optional<WorkDate> workDate = workDateService.findWorkDateById(workDayId);
            WorkDate workDateSave;
            LocalTime workDateStart;
            LocalTime workDateEnd;

            if(workDate.isPresent()) {
                workDateSave = workDate.get();
                workDateStart = workDateSave.getStart_time();
                workDateEnd = workDateSave.getEnd_time();
            } else {
                return ResponseEntity.notFound().build();
            }

            if(intervals != null) {
                // Retrieve existing intervals for the worker
                List<Pair<LocalTime,LocalTime>> existingIntervals = workDateIntervalsService.getIntervalsByWorkerIdAndWorkDateId(workerId, workDayId);

                // Check if overlap exists in the existing intervals
                for(Pair<String,String> interval : intervals) {
                    LocalTime start = LocalTime.parse(interval.getFirst());
                    LocalTime end = LocalTime.parse(interval.getSecond());

                    for (Pair<LocalTime,LocalTime> existingInterval : existingIntervals) {
                        if ( (start.isBefore(existingInterval.getSecond()) && existingInterval.getFirst().isBefore(end))) {
                            return ResponseEntity.badRequest().body("New interval overlaps with existing interval");
                        } else if((start.isBefore(workDateStart) || end.isAfter(workDateEnd))) {
                            return ResponseEntity.badRequest().body("New intervals must be within the working hours of the start and end time");
                        }
                    }
                }

                // If not overlaps found
                for (Pair<String,String> interval : intervals) {
                    LocalTime start = LocalTime.parse(interval.getFirst());
                    LocalTime end = LocalTime.parse(interval.getSecond());
                    WorkDateIntervals workDateInterval = new WorkDateIntervals();
                    workDateInterval.setStartInterval(start);
                    workDateInterval.setEndInterval(end);
                    workDateInterval.setStudent(null);
                    workDateInterval.setTaken(false);
                    workDateInterval.setDescription(null);
                    workDateInterval.setWorkDate(workDateSave);
                    workDateIntervalsService.saveWorkDateInterval(workDateInterval);
                }
            } else {
                ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok("Intervals saved");
        } else {
            throw new WorkerNotFoundException();
        }
    }


    @PutMapping("/{id}/workdays/{date}/interval/{time}/student/{student_id}")
    @Transactional
    public ResponseEntity<String> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time, @PathVariable int student_id, @RequestBody(required = false) Map<String, String> descriptionBody) {

        Optional<Student> student = studentService.getStudentById(student_id);

        if(!student.isPresent())
            throw new StudentNotFoundException();

        Optional<Worker> worker = workerService.getWorkerById(id);

        if (worker.isPresent()) {

            LocalDate appointmentDate = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalTime appointmentTime = LocalTime.parse(time);

            Optional<WorkDate> workDate = worker.get().getWorkDates().stream()
                    .filter(d -> d.getDate().equals(appointmentDate))
                    .findFirst();

            if (workDate.isPresent()) {
                Optional<WorkDateIntervals> workDateInterval = workDate.get().getWorkDateIntervals().stream()
                        .filter(i -> i.getStartInterval().equals(appointmentTime))
                        .findFirst();

                if (workDateInterval.isPresent()) {
                    WorkDateIntervals interval = workDateInterval.get();

                    interval.setTaken(true);
                    interval.setStudent(student.get());

                    if(descriptionBody != null)
                        interval.setDescription(descriptionBody.get("description"));

                    workDateIntervalsService.saveWorkDateInterval(workDateInterval.get());

                    return ResponseEntity.ok("Appointment made successfully.");
                } else {
                    throw new IntervalNotFoundException();
                }
            } else {
                throw new WorkDateNotFoundException();
            }
        } else {
            throw new WorkerNotFoundException();
        }
    }
}
