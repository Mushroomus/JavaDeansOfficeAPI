package com.example.deansoffice.service;

import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.dao.WorkDateIntervalsDAO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkDateIntervalsServiceImpl implements WorkDateIntervalsService {
    private WorkDateIntervalsDAO workDateIntervalsDAO;

    @Autowired
    public WorkDateIntervalsServiceImpl(WorkDateIntervalsDAO theWorkDateIntervalsDAO) {
        workDateIntervalsDAO = theWorkDateIntervalsDAO;
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
}
