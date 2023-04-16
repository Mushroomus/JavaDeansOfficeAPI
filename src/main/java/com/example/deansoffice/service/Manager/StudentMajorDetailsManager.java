package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface StudentMajorDetailsManager {
    ResponseEntity<Map<String,String>> addStudentMajorDetails(Student student, Integer specializationMajorYearId);

    ResponseEntity<Map<String, String>> deleteStudentMajorDetails(Student student, Integer studentMajorDetailsId);

    ResponseEntity<Map<String,String>> editStudentMajorDetails(Student student, Integer studentMajorDetailsId, Integer requestBodyId);
}
