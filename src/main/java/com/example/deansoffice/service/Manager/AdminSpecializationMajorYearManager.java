package com.example.deansoffice.service.Manager;

import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.SpecializationMajorYearPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminSpecializationMajorYearManager {
    List<SpecializationMajorYearDTO> getSpecializationMajorYear();
    ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);
    ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId);
}
