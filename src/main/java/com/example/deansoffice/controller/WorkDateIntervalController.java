package com.example.deansoffice.controller;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.WorkDateIntervalsService;
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
@RequestMapping("/workdate-intervals")
public class WorkDateIntervalController {
    private WorkDateIntervalsService workDateIntervalsService;
    private EmailService emailService;

    WorkDateIntervalController(@Qualifier("workDateIntervalsServiceImpl") WorkDateIntervalsService theWorkDateIntervalsService, EmailService theEmailService) {
        workDateIntervalsService = theWorkDateIntervalsService;
        emailService = theEmailService;
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteListOfWorkDatesIntervals(@RequestBody List<Integer> workDatesIntervalsListId) {
        if(workDatesIntervalsListId != null) {
            workDatesIntervalsListId.forEach(this::delete);
            return ResponseEntity.ok("Intervals deleted");
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkDate(@PathVariable int id) {
        if(delete(id) == 200)
            return ResponseEntity.ok("Interval deleted");
        else
            return ResponseEntity.notFound().build();
    }

    private Integer delete(Integer workDateIntervalId) {
        Optional<WorkDateIntervals> workDateInterval = workDateIntervalsService.findWorkDateIntervalById(workDateIntervalId);
        if(workDateInterval.isPresent()) {
            WorkDateIntervals workDateIntervalDelete = workDateInterval.get();
             if(workDateIntervalDelete.getTaken()) {
                    Map<String, Object> model = new HashMap<>();
                    model.put("studentNameAndSurname", workDateIntervalDelete.getStudent().getName() + " " + workDateIntervalDelete.getStudent().getSurname());
                    model.put("cancelDate", workDateIntervalDelete.getWorkDate().getDate());
                    model.put("cancelInterval", workDateIntervalDelete.getStartInterval() + " - " + workDateIntervalDelete.getEndInterval());

                    try {
                        emailService.sendEmail(workDateIntervalDelete.getStudent().getLogin().getUsername(), "Appointment cancellation", model);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add canceled interval with description
                // delete interval
                return 200;
            } else {
                return 401;
            }
    }
}
