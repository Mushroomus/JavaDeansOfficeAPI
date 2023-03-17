package com.example.deansoffice.controller;

import com.example.deansoffice.service.WorkDateService;
import com.example.deansoffice.service.WorkerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private WorkerService workerService;
    private WorkDateService workDateService;

    public WorkerController(WorkerService theWorkerService, WorkDateService theWorkDateService) {
        workerService = theWorkerService;
        workDateService = theWorkDateService;
    }
}
