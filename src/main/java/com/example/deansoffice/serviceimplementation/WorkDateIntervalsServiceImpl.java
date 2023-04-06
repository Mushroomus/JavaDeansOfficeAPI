package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.CanceledAppointmentsDAO;
import com.example.deansoffice.dao.WorkDateIntervalsDAO;
import com.example.deansoffice.entity.CanceledAppointments;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.service.LocalTimeFormatter;
import com.example.deansoffice.service.WorkDateIntervalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class WorkDateIntervalsServiceImpl implements WorkDateIntervalsService {
    private WorkDateIntervalsDAO workDateIntervalsDAO;
    private CanceledAppointmentsDAO canceledAppointmentsDAO;

    @Autowired
    public WorkDateIntervalsServiceImpl(WorkDateIntervalsDAO theWorkDateIntervalsDAO, CanceledAppointmentsDAO theCanceledAppointmentsDAO) {
        workDateIntervalsDAO = theWorkDateIntervalsDAO;
        canceledAppointmentsDAO = theCanceledAppointmentsDAO;
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
    public ResponseEntity<String> cancelAppointment(Integer studentId, Integer appointmentId) {
        Optional<WorkDateIntervals> workDateIntervals = workDateIntervalsDAO.findById(appointmentId);

        if(workDateIntervals.isPresent() && workDateIntervals.get().getStudent().getId() == studentId) {
            WorkDateIntervals workDateIntervalsEdit = workDateIntervals.get();
            CanceledAppointments canceledAppointment = new CanceledAppointments();

            canceledAppointment.setStudent(workDateIntervalsEdit.getStudent());
            canceledAppointment.setWorkDate(workDateIntervalsEdit.getWorkDate());
            canceledAppointment.setStartInterval(workDateIntervalsEdit.getStartInterval());
            canceledAppointment.setEndInterval(workDateIntervalsEdit.getEndInterval());

            if(workDateIntervalsEdit.getDescription() != null)
                canceledAppointment.setDescription(workDateIntervalsEdit.getDescription());
            canceledAppointmentsDAO.save(canceledAppointment);

            workDateIntervalsEdit.setStudent(null);
            workDateIntervalsEdit.setTaken(false);
            workDateIntervalsDAO.save(workDateIntervalsEdit);

            return ResponseEntity.ok("Appointment canceled");
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<Object[]>> findByStudentIdAndStartInvervalAndEndInterval(int studentId, String startIntervalString, String endIntervalString, LocalDate startDate, LocalDate endDate, Integer workerId) {

        LocalTime startInterval = null;
        LocalTime endInterval = null;

        try {
            if (startIntervalString != null) {
                startInterval = LocalTimeFormatter.parse(startIntervalString);
            }
            if (endIntervalString != null) {
                endInterval = LocalTimeFormatter.parse(endIntervalString);
            }

            List<Object[]> response = workDateIntervalsDAO.findByStudentIdAndStartInvervalAndEndInterval(studentId, startInterval, endInterval, startDate, endDate, workerId);
            return ResponseEntity.ok(response);

        } catch (DateTimeParseException e) {
            return  ResponseEntity.badRequest().build();
        }
    }
}
