package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.StudentMajorDetailsDAO;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.service.StudentMajorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentMajorDetailsServiceImpl implements StudentMajorDetailsService {
    private StudentMajorDetailsDAO studentMajorDetailsDAO;

    @Autowired
    public StudentMajorDetailsServiceImpl(StudentMajorDetailsDAO theStudentMajorDetailsDAO) {
        studentMajorDetailsDAO = theStudentMajorDetailsDAO;
    }

    @Override
    public void addStudentMajorDetails(StudentMajorDetails studentMajorDetails) {
        studentMajorDetailsDAO.save(studentMajorDetails);
    }
}
