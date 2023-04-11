package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.model.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public interface WorkerWorkDateIntervalsManager {
    void createIntervals(WorkDate workDate, int interval_minutes);
    List<Pair<LocalTime, LocalTime>> getIntervalsByWorkerIdAndWorkDateId(int workerId, int workDateId);
    void saveWorkDateInterval(WorkDateIntervals workDateIntervals);
}
