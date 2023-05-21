package com.example.deansoffice.service;

import com.example.deansoffice.dto.StudentMajorDetailsDTO;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentMajorDetailsService {
    void addStudentMajorDetails(StudentMajorDetails studentMajorDetails);
    List<StudentMajorDetailsDTO> getStudentMajorDetails(Student student);
}
