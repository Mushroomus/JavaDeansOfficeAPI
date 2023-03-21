package com.example.deansoffice.service;

import com.example.deansoffice.dto.MajorDetailsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MajorDetailsService {
    List<MajorDetailsDTO> getAllMajors();

    List<MajorDetailsDTO> getMajorsByYear(int year);
}
