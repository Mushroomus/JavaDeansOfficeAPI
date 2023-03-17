package com.example.deansoffice.controller;

import com.example.deansoffice.service.WorkDateService;
import com.example.deansoffice.service.WorkerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private WorkerService workerService;
    private WorkDateService workDateService;

    public WorkerController(WorkerService theWorkerService, WorkDateService theWorkDateService) {
        workerService = theWorkerService;
        workDateService = theWorkDateService;
    }

    @GetMapping("/{id}/workdays/{year}/{month}")
    public ResponseEntity<List<Integer>> getWorkDaysForMonthAndYear(@PathVariable int id, @PathVariable int year, @PathVariable int month) {
        List<Integer> workDays = workerService.getWorkDaysForMonthAndYear(id, year, month);
        return ResponseEntity.ok(workDays);
    }
}
