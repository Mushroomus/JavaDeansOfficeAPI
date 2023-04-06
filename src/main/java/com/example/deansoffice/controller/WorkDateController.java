package com.example.deansoffice.controller;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.WorkDateIntervalsService;
import com.example.deansoffice.service.WorkDateService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private EmailService emailService;

    WorkDateController(WorkDateService theWorkDateService, EmailService theEmailService) {
        workDateService = theWorkDateService;
        emailService = theEmailService;
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteListOfWorkDates(@RequestBody List<Integer> workDatesListId) {

        if(workDatesListId != null) {
            workDatesListId.forEach( (workDateId) -> {
                    Optional<WorkDate> workDate = workDateService.findWorkDateById(workDateId);
                    if(workDate.isPresent()) {
                        WorkDate workDateDelete = workDate.get();
                        workDateDelete.getWorkDateIntervals().forEach((interval) -> {

                            if(interval.getTaken()) {
                                Map<String, Object> model = new HashMap<>();
                                model.put("studentNameAndSurname", interval.getStudent().getName() + " " + interval.getStudent().getSurname());
                                model.put("cancelDate", workDateDelete.getDate());
                                model.put("cancelInterval", interval.getStartInterval() + " - " + interval.getEndInterval());

                                try {
                                    emailService.sendEmail(interval.getStudent().getLogin().getUsername(), "Appointment cancellation", model);
                                } catch (MessagingException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            // add canceled interval with description
                            // delete interval
                        });
                    }
            });
            return ResponseEntity.ok("Work days deleted");
        }
        return ResponseEntity.noContent().build();
    }
}
