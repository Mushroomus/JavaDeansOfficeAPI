package com.example.deansoffice.service.Fetcher;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface WorkDateFetcher {
    ResponseEntity<List<LocalDate>> getWorkDates(Integer workerId);
}
