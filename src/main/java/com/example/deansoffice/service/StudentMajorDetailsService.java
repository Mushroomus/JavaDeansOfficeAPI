package com.example.deansoffice.service;

import com.example.deansoffice.entity.StudentMajorDetails;
import org.springframework.stereotype.Service;

@Service
public interface StudentMajorDetailsService {
    void addStudentMajorDetails(StudentMajorDetails studentMajorDetails);
}
