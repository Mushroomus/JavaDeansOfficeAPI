package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.WorkDateIntervalsDAO;
import com.example.deansoffice.entity.CanceledAppointments;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.exception.AccessForbiddenException;
import com.example.deansoffice.exception.BadRequestException;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Pair;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import com.example.deansoffice.service.EmailService;
import com.example.deansoffice.service.Manager.StudentWorkDateIntervalsManager;
import com.example.deansoffice.service.Manager.WorkDateIntervalsCanceledAppointmentsManager;
import com.example.deansoffice.service.Manager.WorkerWorkDateIntervalsManager;
import com.example.deansoffice.service.Utils.LocalTimeFormatter;
import com.example.deansoffice.service.WorkDateIntervalsService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class WorkDateIntervalsServiceImpl implements WorkDateIntervalsService, WorkerWorkDateIntervalsManager, StudentWorkDateIntervalsManager {
    private WorkDateIntervalsDAO workDateIntervalsDAO;
    private WorkDateIntervalsCanceledAppointmentsManager workDateIntervalsCanceledAppointmentsManager;
    private EmailService emailService;

    @Autowired
    public WorkDateIntervalsServiceImpl(WorkDateIntervalsDAO theWorkDateIntervalsDAO, WorkDateIntervalsCanceledAppointmentsManager theWorkDateIntervalsCanceledAppointmentsManager, EmailService theEmailService) {
        workDateIntervalsDAO = theWorkDateIntervalsDAO;
        workDateIntervalsCanceledAppointmentsManager = theWorkDateIntervalsCanceledAppointmentsManager;
        emailService = theEmailService;
    }

    public void createIntervals(WorkDate workDate, int interval_minutes) {
        List<WorkDateIntervals> intervals = new ArrayList<>();

        LocalTime start = workDate.getStart_time();
        LocalTime end = workDate.getEnd_time();

        while (start.isBefore(end)) {
            LocalTime intervalEnd = start.plusMinutes(interval_minutes);
            if (intervalEnd.isAfter(end)) {
                intervalEnd = end;
            }

            WorkDateIntervals interval = new WorkDateIntervals();
            interval.setWorkDate(workDate);
            interval.setStartInterval(start);
            interval.setEndInterval(intervalEnd);
            interval.setTaken(false);
            intervals.add(interval);

            start = intervalEnd;
        }

        workDateIntervalsDAO.saveAll(intervals);
    }

    @Override
    public void saveWorkDateInterval(WorkDateIntervals workDateIntervals) {
        workDateIntervalsDAO.save(workDateIntervals);
    }

    @Override
    public ResponseEntity<Response> cancelAppointment(Integer studentId, Integer appointmentId) {

        try {
            Optional<WorkDateIntervals> workDateIntervals = workDateIntervalsDAO.findById(appointmentId);

            if (workDateIntervals.isEmpty()) {
                throw new RecordNotFoundException("Appointment not found");
            }

            if (workDateIntervals.get().getStudent().getId() == studentId) {
                throw new AccessForbiddenException();
            }

            WorkDateIntervals workDateIntervalsEdit = workDateIntervals.get();
            CanceledAppointments canceledAppointment = new CanceledAppointments();

            canceledAppointment.setStudent(workDateIntervalsEdit.getStudent());
            canceledAppointment.setWorkDate(workDateIntervalsEdit.getWorkDate());
            canceledAppointment.setStartInterval(workDateIntervalsEdit.getStartInterval());
            canceledAppointment.setEndInterval(workDateIntervalsEdit.getEndInterval());

            if (workDateIntervalsEdit.getDescription() != null)
                canceledAppointment.setDescription(workDateIntervalsEdit.getDescription());
            workDateIntervalsCanceledAppointmentsManager.saveCanceledAppointment(canceledAppointment);

            workDateIntervalsEdit.setStudent(null);
            workDateIntervalsEdit.setTaken(false);
            workDateIntervalsDAO.save(workDateIntervalsEdit);

            return ResponseEntity.status(HttpStatus.OK).body(new Response("Appointment canceled"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to cancel appointment");
        }

    }

    @Override
    public ResponseEntity<List<StudentAppointmentGetResponse>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString, LocalDate startDate, LocalDate endDate, Integer workerId) {

        LocalTime startInterval = null;
        LocalTime endInterval = null;

        try {
            if (startIntervalString != null) {
                startInterval = LocalTimeFormatter.parse(startIntervalString);
            }
            if (endIntervalString != null) {
                endInterval = LocalTimeFormatter.parse(endIntervalString);
            }

            List<StudentAppointmentGetResponse> response = workDateIntervalsDAO.findByStudentIdAndStartInvervalAndEndInterval(studentId, startInterval, endInterval, startDate, endDate, workerId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch(DateTimeParseException e) {
            throw new BadRequestException("Failed to parse date");
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to get student appointments");
        }
    }

    @Override
    public Optional<WorkDateIntervals> findWorkDateIntervalById(Integer workDateIntervalId) {
        return workDateIntervalsDAO.findById(workDateIntervalId);
    }

    @Override
    public List<Pair<LocalTime, LocalTime>> getIntervalsByWorkerIdAndWorkDateId(int workerId, int workDateId) {
        return workDateIntervalsDAO.getIntervalsByWorkerIdAndDate(workerId, workDateId);
    }

    private void deleteInterval(Integer workerId, Integer workDateIntervalId) {
        try {
            Optional<WorkDateIntervals> workDateInterval = workDateIntervalsDAO.findById(workDateIntervalId);

            if (workDateInterval.isPresent()) {
                WorkDateIntervals workDateIntervalDelete = workDateInterval.get();

                if(workDateIntervalDelete.getWorkDate().getWorker().getId() != workerId) {
                    throw new AccessForbiddenException();
                }

                if (workDateIntervalDelete.getTaken()) {
                    Map<String, Object> model = new HashMap<>();
                    model.put("studentNameAndSurname", workDateIntervalDelete.getStudent().getName() + " " + workDateIntervalDelete.getStudent().getSurname());
                    model.put("cancelDate", workDateIntervalDelete.getWorkDate().getDate());
                    model.put("cancelInterval", workDateIntervalDelete.getStartInterval() + " - " + workDateIntervalDelete.getEndInterval());

                    try {
                        emailService.sendEmail(workDateIntervalDelete.getStudent().getLogin().getUsername(), "Appointment cancellation", model, "cancel-appointment");
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
                // add canceled interval with description
                // delete interval
            } else {
                throw new RecordNotFoundException("Interval not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to remove interval");
        }
    }

    @Override
    public ResponseEntity<Response> deleteSingleWorkDateInterval(Integer workerId, Integer workDateIntervalId) {
       deleteInterval(workerId, workDateIntervalId);
       return ResponseEntity.status(HttpStatus.OK).body(new Response("Interval deleted"));
    }

    @Override
    public ResponseEntity<Response> deleteListOfWorkDatesIntervals(Integer workerId, List<Integer> workDatesIntervalsListId) {
        if(workDatesIntervalsListId != null && !workDatesIntervalsListId.isEmpty()) {
            workDatesIntervalsListId.forEach(x-> deleteInterval(workerId, x));
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Intervals deleted"));
        } else {
            throw new BadRequestException("Work date interval list is empty");
        }
    }
}
