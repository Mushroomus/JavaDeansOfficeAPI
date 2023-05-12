package com.example.deansoffice.service.Manager;

import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AdminMajorYearManager {
    List<MajorYearDTO> getMajorYears();
    ResponseEntity<Response> addMajorYear(Integer year);
    ResponseEntity<Response> deleteMajorYear(Integer id);
}
