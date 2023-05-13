package com.example.deansoffice.service;

import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.model.Role;
import com.example.deansoffice.record.StudentAppointmentGetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface StudentService {
    StudentDTO getStudent(Integer studentId);
    Login addStudent(Student student, String username, String password, Role role);
    ResponseEntity<Response> updateStudent(Integer studentId, Student student);
    ResponseEntity<Response> cancelAppointment(int studentId, int appointmentId);

    ResponseEntity<List<StudentAppointmentGetResponse>> getAppointments(int studentId, String startInterval, String endInterval, LocalDate startDate, LocalDate endDate, Integer workerId);

    ResponseEntity<Response> addMajorDetails(Integer studentId, Integer specializationMajorYearId);

    ResponseEntity<Response> deleteMajorDetails(Integer studentId, Integer majorDetailsId);

    ResponseEntity<List<SpecializationMajorYearDTO>> getStudentSpecializationMajorYear(int studentId);

    ResponseEntity<Response> editMajorDetails(Integer studentId, Integer studentMajorDetailsId, Integer requestBodyId);
}
