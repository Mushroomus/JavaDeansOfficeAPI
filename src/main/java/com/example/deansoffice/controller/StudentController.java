package com.example.deansoffice.controller;

import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.model.RegisterStudent;
import com.example.deansoffice.service.SpecializationMajorYearService;
import com.example.deansoffice.service.SpecializationService;
import com.example.deansoffice.service.StudentMajorDetailsService;
import com.example.deansoffice.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;
    private StudentMajorDetailsService studentMajorDetailsService;
    private SpecializationService specializationService;
    private SpecializationMajorYearService specializationMajorYearService;
    StudentController(StudentService theStudentService, StudentMajorDetailsService theStudentMajorDetailsService, SpecializationService theSpecializationService, SpecializationMajorYearService theSpecializationMajorYearService) {
        studentService = theStudentService;
        studentMajorDetailsService = theStudentMajorDetailsService;
        specializationService = theSpecializationService;
        specializationMajorYearService = theSpecializationMajorYearService;
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:3000")
    @Transactional
    public ResponseEntity<Map<String,String>> createWorker(@RequestBody RegisterStudent registerStudent) {

        Map<String,String> response = new HashMap<>(1);

        Student student = new Student();
        student.setName(registerStudent.getName());
        student.setSurname(registerStudent.getSurname());

        studentService.addStudent(student, registerStudent.getUsername(), registerStudent.getPassword());

        Integer specializationId = specializationService.findBySpecializationByNameAndCourse(registerStudent.getMajor(), registerStudent.getSpecialization()).getId();

        SpecializationMajorYear specializationMajorYear = specializationMajorYearService.findSpecializationMajorYearByMajorYearIdAndSpecializationId(registerStudent.getYear(), specializationId);//specializationId);

        if (specializationMajorYear!= null) {
            StudentMajorDetails studentMajorDetails = new StudentMajorDetails();
            studentMajorDetails.setStudent(student);
            studentMajorDetails.setSpecializationMajorYear(specializationMajorYear);

            studentMajorDetailsService.addStudentMajorDetails(studentMajorDetails);
        } else {
            response.put("response", "Something went wrong");
            return ResponseEntity.status(404).body(response);
        }

        response.put("response", "Student registered");
        return ResponseEntity.status(201).body(response);
    }

}
