package com.example.deansoffice.service;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import org.springframework.stereotype.Service;

@Service
public interface WorkDateIntervalsService {
    void createIntervals(WorkDate workDate, int interval_minutes);

    void saveWorkDateInterval(WorkDateIntervals workDateIntervals);
}
