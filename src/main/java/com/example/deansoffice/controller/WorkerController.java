package com.example.deansoffice.controller;

import com.example.deansoffice.dto.NewWorkDayRequest;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.exception.IntervalNotFoundException;
import com.example.deansoffice.exception.WorkDateNotFoundException;
import com.example.deansoffice.exception.WorkerNotFoundException;
import com.example.deansoffice.service.WorkDateIntervalsService;
import com.example.deansoffice.service.WorkDateService;
import com.example.deansoffice.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private WorkerService workerService;
    private WorkDateService workDateService;

    private WorkDateIntervalsService workDateIntervalsService;

    public WorkerController(WorkerService theWorkerService, WorkDateService theWorkDateService, WorkDateIntervalsService theWorkDateIntervalsService) {
        workerService = theWorkerService;
        workDateService = theWorkDateService;
        workDateIntervalsService = theWorkDateIntervalsService;
    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<WorkerDTO>> getWorkers() {
        List<WorkerDTO> workersDTO = workerService.getWorkers();
        return ResponseEntity.ok(workersDTO);
    }

    @PostMapping("/{id}/workdays")
    public ResponseEntity<String> newWorkDayForWorker(@PathVariable int id, @RequestBody NewWorkDayRequest request) {
        Optional<Worker> worker = workerService.getWorkerById(id);

        if(worker.isPresent()) {
            WorkDate workDate = workDateService.newWorkDateForUser(worker.get(), request.getDate(), request.getStartTime(), request.getEndTime());
            workDateIntervalsService.createIntervals(workDate, 10);
            return ResponseEntity.ok("Work day saved");
        } else {
            throw new WorkerNotFoundException();
        }
    }

    @PutMapping("/{id}/workdays/{date}/interval/{time}")
    public ResponseEntity<String> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time) {
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
                workDate.get().getWorkDateIntervals().forEach(i -> System.out.println(i.getStartInterval()));

                Optional<WorkDateIntervals> workDateInterval = workDate.get().getWorkDateIntervals().stream()
                        .filter(i -> i.getStartInterval().equals(appointmentTime))
                        .findFirst();

                if (workDateInterval.isPresent()) {
                    workDateInterval.get().setTaken(true);
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
