package com.example.deansoffice.service.Manager;

import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminSpecializationMajorYearManager {
    ResponseEntity<Map<String,String>> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);

    ResponseEntity<Map<String, String>> deleteSpecializationMajorYear(Integer specializationMajorYearId);
}
