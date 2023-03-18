package com.example.deansoffice.service;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public interface WorkDateService {
    WorkDate newWorkDateForUser(Worker worker, LocalDate date, LocalTime startTime, LocalTime endTime);
}
