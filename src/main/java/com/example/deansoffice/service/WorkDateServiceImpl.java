package com.example.deansoffice.service;

import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.dao.WorkDateIntervalsDAO;
import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class WorkDateServiceImpl implements  WorkDateService {
    private WorkDateDAO workDateDAO;

    private WorkDateIntervalsDAO workDateIntervalsDAO;

    @Autowired
    public WorkDateServiceImpl(WorkDateDAO theWorkDateDAO, WorkDateIntervalsDAO theWorkDateIntervalsDAO) {
        workDateDAO = theWorkDateDAO;
        workDateIntervalsDAO = theWorkDateIntervalsDAO;
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
}
