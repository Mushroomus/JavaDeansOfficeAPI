package com.example.deansoffice.service.Manager;

import com.example.deansoffice.dto.StudentMajorDetailsDTO;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentMajorDetailsManager {
    ResponseEntity<Response> addStudentMajorDetails(Student student, Integer specializationMajorYearId);

    ResponseEntity<Response> deleteStudentMajorDetails(Student student, Integer studentMajorDetailsId);

    ResponseEntity<Response> editStudentMajorDetails(Student student, Integer studentMajorDetailsId, Integer requestBodyId);

    List<StudentMajorDetailsDTO> getStudentMajorDetails(Student student);
}
