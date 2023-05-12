package com.example.deansoffice.service.Manager;

import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Service
public interface AdminSpecializationMajorYearManager {
    List<SpecializationMajorYearDTO> getSpecializationMajorYear();
    ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest);
    ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId);
}
