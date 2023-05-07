package com.example.deansoffice.service.Manager;

import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AdminSpecializationMajorYearManager {
    ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);

    ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId);
}
