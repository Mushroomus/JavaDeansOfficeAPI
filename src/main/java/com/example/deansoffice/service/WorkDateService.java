package com.example.deansoffice.service;

import com.example.deansoffice.entity.WorkDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface WorkDateService {
    Optional<WorkDate> findWorkDateById(Integer workDateId);

    ResponseEntity<String> deleteSingleWorkDate(int id);

    ResponseEntity<String> deleteListOfWorkDates(List<Integer> workDatesListId);
}
