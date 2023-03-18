package com.example.deansoffice.controller;

import com.example.deansoffice.dto.NewWorkDayRequest;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.entity.Worker;
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
        System.out.println("Done Request");
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
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/workdays/{year}/{month}")
    public ResponseEntity<List<Integer>> getWorkDaysForMonthAndYear(@PathVariable int id, @PathVariable int year, @PathVariable int month) {
        List<Integer> workDays = workerService.getWorkDaysForMonthAndYear(id, year, month);
        return ResponseEntity.ok(workDays);
    }

    @PutMapping("/{id}/workdays/{date}/interval/{time}")
    public ResponseEntity<String> makeAppointment(@PathVariable int id, @PathVariable long date, @PathVariable String time) {
        Optional<Worker> worker = workerService.getWorkerById(id);

        if (worker.isPresent()) {

            LocalDate appointmentDate = Instant.ofEpochMilli(date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            System.out.println(appointmentDate);

            LocalTime appointmentTime = LocalTime.parse(time);

            // Find the WorkDate corresponding to the appointment date
            Optional<WorkDate> workDate = worker.get().getWorkDates().stream()
                    .filter(d -> d.getDate().equals(appointmentDate))
                    .findFirst();

            if (workDate.isPresent()) {
                System.out.println(appointmentTime);
                // Find the WorkDateInterval corresponding to the appointment time

                System.out.println(workDate.get().getId());
                workDate.get().getWorkDateIntervals().stream().forEach(i -> System.out.println(i.getStartInterval()));

                Optional<WorkDateIntervals> workDateInterval = workDate.get().getWorkDateIntervals().stream()
                        .filter(i -> i.getStartInterval().equals(appointmentTime))
                        .findFirst();

                if (workDateInterval.isPresent()) {
                    // Update the taken attribute
                    System.out.println("Time found");
                    workDateInterval.get().setTaken(true);

                    // Save the changes
                    workDateIntervalsService.saveWorkDateInterval(workDateInterval.get());

                    return ResponseEntity.ok("Appointment made successfully.");
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
