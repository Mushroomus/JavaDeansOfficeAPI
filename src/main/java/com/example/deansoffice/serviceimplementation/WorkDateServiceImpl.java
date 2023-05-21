package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.exception.AccessForbiddenException;
import com.example.deansoffice.exception.BadRequestException;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.Fetcher.WorkDateFetcher;
import com.example.deansoffice.service.Manager.WorkerWorkDateManager;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkDateServiceImpl implements  WorkerWorkDateManager, WorkDateFetcher {
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
    public ResponseEntity<List<LocalDate>> getWorkDates(Integer workerId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(workDateDAO.getWorkDatesGreaterThanCurrentDate(workerId));
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get worker work dates");
        }
    }

    @Override
    public Optional<WorkDate> findWorkDateById(Integer workDateId) {
        return workDateDAO.findById(workDateId);
    }

    private void deleteWorkDate(Integer workerId, Integer workDateId) {
        try {
            Optional<WorkDate> workDate = workDateDAO.findById(workDateId);
            if (workDate.isPresent()) {
                WorkDate workDateDelete = workDate.get();

                if(workDateDelete.getWorker().getId() != workerId ) {
                    throw new AccessForbiddenException();
                }

                workDateDelete.getWorkDateIntervals().forEach((interval) -> {

                    if (interval.getTaken()) {
                        Map<String, Object> model = new HashMap<>();
                        model.put("studentNameAndSurname", interval.getStudent().getName() + " " + interval.getStudent().getSurname());
                        model.put("cancelDate", workDateDelete.getDate());
                        model.put("cancelInterval", interval.getStartInterval() + " - " + interval.getEndInterval());

                        try {
                            emailService.sendEmail(interval.getStudent().getLogin().getUsername(), "Appointment cancellation", model, "cancel-appointment");
                        } catch (MessagingException e) {
                            throw new InternalServerErrorException("Error while sending an email");
                        }
                    }
                });
                workDateDAO.deleteById(workDateId);
            } else {
                throw new RecordNotFoundException("Work date not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work date");
        }
    }
    @Override
    public ResponseEntity<Response> deleteSingleWorkDate(Integer workerId, Integer workdayId) {
        try {
            deleteWorkDate(workerId, workdayId);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Worker work day deleted"));
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work date");
        }
    }

    @Override
    public ResponseEntity<Response> deleteListOfWorkDates(Integer workerId, List<Integer> workDatesListId) {
        try {
            if (workDatesListId != null && !workDatesListId.isEmpty()) {
                workDatesListId.forEach(x -> deleteWorkDate(workerId, x));
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Worker work days deleted"));
            } else {
                throw new BadRequestException("List of work dates is empty");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete worker work dates");
        }
    }
}
