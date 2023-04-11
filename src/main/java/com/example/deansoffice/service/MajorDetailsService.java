package com.example.deansoffice.service;

import com.example.deansoffice.dto.MajorDetailsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MajorDetailsService {
    ResponseEntity<Map<String,List<MajorDetailsDTO>>> getAllMajors(Integer year);
}
