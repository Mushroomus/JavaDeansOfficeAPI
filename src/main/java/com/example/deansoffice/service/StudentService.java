package com.example.deansoffice.service;

import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.model.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StudentService {
    Login addStudent(Student student, String username, String password, Role role);
    Optional<Student> getStudentById(int id);
}
