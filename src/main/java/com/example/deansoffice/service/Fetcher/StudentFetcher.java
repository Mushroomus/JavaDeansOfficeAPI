package com.example.deansoffice.service.Fetcher;

import com.example.deansoffice.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StudentFetcher {
    Optional<Student> getStudentById(int id);
    List<Student> getStudents();
}
