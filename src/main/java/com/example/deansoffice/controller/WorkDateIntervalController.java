package com.example.deansoffice.controller;

import com.example.deansoffice.service.WorkDateIntervalsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/workdate-intervals")
public class WorkDateIntervalController {
    private WorkDateIntervalsService workDateIntervalsService;

    WorkDateIntervalController(@Qualifier("workDateIntervalsServiceImpl") WorkDateIntervalsService theWorkDateIntervalsService) {
        workDateIntervalsService = theWorkDateIntervalsService;
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteListOfWorkDatesIntervals(@RequestBody List<Integer> workDatesIntervalsListId) {
        return workDateIntervalsService.deleteListOfWorkDatesIntervals(workDatesIntervalsListId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkDate(@PathVariable int id) {
        return workDateIntervalsService.deleteWorkDate(id);
    }
}
