package com.example.deansoffice.service;

import com.example.deansoffice.entity.Student;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StudentService {
    void addStudent(Student student, String username, String password);
    Optional<Student> getStudentById(int id);
}
