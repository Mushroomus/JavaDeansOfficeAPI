package com.example.deansoffice.service.Manager;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminMajorYearManager {
    ResponseEntity<Map<String,String>> addMajorYear(Integer year);

    ResponseEntity<Map<String, String>> deleteMajorYear(Integer id);
}
