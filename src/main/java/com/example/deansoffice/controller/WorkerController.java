package com.example.deansoffice.controller;

import com.example.deansoffice.dto.NewWorkDayRequest;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.exception.IntervalNotFoundException;
import com.example.deansoffice.exception.StudentNotFoundException;
import com.example.deansoffice.exception.WorkDateNotFoundException;
import com.example.deansoffice.exception.WorkerNotFoundException;
import com.example.deansoffice.service.StudentService;
import com.example.deansoffice.service.WorkDateIntervalsService;
import com.example.deansoffice.service.WorkDateService;
import com.example.deansoffice.service.WorkerService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.deansoffice.entity.Student;

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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<WorkerDTO>> getWorkers() {
        List<WorkerDTO> workersDTO = workerService.getWorkers();
        return ResponseEntity.ok(workersDTO);
    }

    @GetMapping("/{id}/workdays/{date}/intervals")
    @CrossOrigin(origins = "http://localhost:3000")
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

    @PostMapping("/{id}/workdays")
    @CrossOrigin(origins = "http://localhost:3000")
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

    @PutMapping("/{id}/workdays/{date}/interval/{time}/student/{student_id}")
    @CrossOrigin(origins = "http://localhost:3000")
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
