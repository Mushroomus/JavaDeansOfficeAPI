package com.example.deansoffice.controller;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.WorkDateService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/workdate")
public class WorkDateController {
    private WorkDateService workDateService;

    WorkDateController(WorkDateService theWorkDateService) {
        workDateService = theWorkDateService;
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteListOfWorkDates(@RequestBody List<Integer> workDatesListId) {
        return workDateService.deleteListOfWorkDates(workDatesListId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkDate(@PathVariable int id) {
        return workDateService.deleteSingleWorkDate(id);
    }
}
