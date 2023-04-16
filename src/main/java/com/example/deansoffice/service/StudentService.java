package com.example.deansoffice.service;

import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public interface StudentService {
    Login addStudent(Student student, String username, String password, Role role);

    ResponseEntity<String> cancelAppointment(int studentId, int appointmentId);

    ResponseEntity<List<Object[]>> getAppointments(int studentId, String startInterval, String endInterval, LocalDate startDate, LocalDate endDate, Integer workerId);

    ResponseEntity<Map<String,String>> addMajorDetails(Integer studentId, Integer specializationMajorYearId);

    ResponseEntity<Map<String, String>> deleteMajorDetails(Integer studentId, Integer majorDetailsId);

    ResponseEntity<Map<String, ?>> getStudentSpecializationMajorYear(int studentId);

    ResponseEntity<Map<String, String>> editMajorDetails(Integer studentId, Integer studentMajorDetailsId, Integer requestBodyId);
}
