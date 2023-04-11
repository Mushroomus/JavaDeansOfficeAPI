package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.Manager.WorkerWorkDateManager;
import com.example.deansoffice.service.WorkDateService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkDateServiceImpl implements WorkDateService, WorkerWorkDateManager {
    private WorkDateDAO workDateDAO;

    private EmailService emailService;

    @Autowired
    public WorkDateServiceImpl(WorkDateDAO theWorkDateDAO, EmailService theEmailService) {
        workDateDAO = theWorkDateDAO;
        emailService = theEmailService;
    }

    public WorkDate newWorkDateForUser(Worker worker, LocalDate date, LocalTime startTime, LocalTime endTime) {
        WorkDate workDate = new WorkDate();
        workDate.setWorker(worker);
        workDate.setDate(date);
        workDate.setStart_time(startTime);
        workDate.setEnd_time(endTime);

        workDateDAO.save(workDate);
        return workDate;
    }

    @Override
    public Optional<WorkDate> findWorkDateById(Integer workDateId) {
        return workDateDAO.findById(workDateId);
    }

    private Integer deleteWorkDate(Integer workDateId) {
        Optional<WorkDate> workDate = workDateDAO.findById(workDateId);
        if(workDate.isPresent()) {
            WorkDate workDateDelete = workDate.get();
            workDateDelete.getWorkDateIntervals().forEach((interval) -> {

                if(interval.getTaken()) {
                    Map<String, Object> model = new HashMap<>();
                    model.put("studentNameAndSurname", interval.getStudent().getName() + " " + interval.getStudent().getSurname());
                    model.put("cancelDate", workDateDelete.getDate());
                    model.put("cancelInterval", interval.getStartInterval() + " - " + interval.getEndInterval());

                    try {
                        emailService.sendEmail(interval.getStudent().getLogin().getUsername(), "Appointment cancellation", model, "cancel-appointment");
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add canceled interval with description
                // delete interval
            });
            return 200;
        } else {
            return 401;
        }
    }
    @Override
    public ResponseEntity<String> deleteSingleWorkDate(int id) {
        if(deleteWorkDate(id) == 200)
            return ResponseEntity.ok("Work day deleted");
        else
            return ResponseEntity.notFound().build();
    }
    @Override
    public ResponseEntity<String> deleteListOfWorkDates(List<Integer> workDatesListId) {
        if(workDatesListId != null) {
            workDatesListId.forEach(this::deleteWorkDate);
            return ResponseEntity.ok("Work days deleted");
        }
        return ResponseEntity.noContent().build();
    }
}
