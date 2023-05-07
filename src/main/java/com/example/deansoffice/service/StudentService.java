package com.example.deansoffice.service;

import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Response;
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

    ResponseEntity<Response> cancelAppointment(int studentId, int appointmentId);

    ResponseEntity<List<Object[]>> getAppointments(int studentId, String startInterval, String endInterval, LocalDate startDate, LocalDate endDate, Integer workerId);

    ResponseEntity<Response> addMajorDetails(Integer studentId, Integer specializationMajorYearId);

    ResponseEntity<Response> deleteMajorDetails(Integer studentId, Integer majorDetailsId);

    ResponseEntity<Map<String, ?>> getStudentSpecializationMajorYear(int studentId);

    ResponseEntity<Response> editMajorDetails(Integer studentId, Integer studentMajorDetailsId, Integer requestBodyId);
}
