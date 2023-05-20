package com.example.deansoffice.service.Manager;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.record.WorkDayIntervalsGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentWorkerManager {
    List<WorkerDTO> getWorkers();
    List<WorkerDTO> getWorkersBySpecializations(List<Integer> studentSpecializationIdList);
    ResponseEntity<WorkDayIntervalsGetResponse> getWorkDayIntervals(int id, long date);
}
