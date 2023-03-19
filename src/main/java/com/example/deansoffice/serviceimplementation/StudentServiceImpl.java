package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.StudentMapper;
import com.example.deansoffice.component.WorkerMapper;
import com.example.deansoffice.dao.StudentDAO;
import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.dao.WorkerDAO;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentDAO studentDAO;
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentDAO theStudentDAO) {
        studentDAO = theStudentDAO;
    }

    @Override
    public Optional<Student> getStudentById(int id) {
        return studentDAO.findById(id);
    }
}
