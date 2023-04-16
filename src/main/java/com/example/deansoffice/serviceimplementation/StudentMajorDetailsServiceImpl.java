package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.StudentMajorDetailsDAO;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.service.Fetcher.SpecializationMajorYearFetcher;
import com.example.deansoffice.service.Manager.StudentMajorDetailsManager;
import com.example.deansoffice.service.StudentMajorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentMajorDetailsServiceImpl implements StudentMajorDetailsService, StudentMajorDetailsManager {
    private StudentMajorDetailsDAO studentMajorDetailsDAO;
    private SpecializationMajorYearFetcher specializationMajorYearFetcher;

    @Autowired
    public StudentMajorDetailsServiceImpl(StudentMajorDetailsDAO theStudentMajorDetailsDAO, SpecializationMajorYearFetcher theSpecializationMajorYearFetcher) {
        studentMajorDetailsDAO = theStudentMajorDetailsDAO;
        specializationMajorYearFetcher = theSpecializationMajorYearFetcher;
    }

    @Override
    public ResponseEntity<Map<String,String>> addStudentMajorDetails(Student student, Integer specializationMajorYearId) {

        Optional<SpecializationMajorYear> specializationMajorYearOptional = specializationMajorYearFetcher.getSpecializationMajorYear(specializationMajorYearId);
        Map<String,String> response = new HashMap<>();

        if(specializationMajorYearOptional.isPresent()) {
            StudentMajorDetails studentMajorDetails = StudentMajorDetails.builder()
                                                .student(student)
                                                .specializationMajorYear(specializationMajorYearOptional.get())
                                                .build();

            studentMajorDetailsDAO.save(studentMajorDetails);
            if(studentMajorDetails.getId() > 0) {
                response.put("response", "Major Details added");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("response", "Failed to add major details");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("response", "Specialization Major Year not found");
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteStudentMajorDetails(Student student, Integer studentMajorDetailsId) {
        Optional<StudentMajorDetails> optionalStudentMajorDetails = studentMajorDetailsDAO.findById(studentMajorDetailsId);
        Map<String, String> response = new HashMap<>();

        if (optionalStudentMajorDetails.isEmpty()) {
            response.put("response", "Major Details not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if(optionalStudentMajorDetails.get().getStudent().getId() != student.getId()) {
            response.put("response", "Access forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        StudentMajorDetails deleteStudentMajorDetails = optionalStudentMajorDetails.get();
        deleteStudentMajorDetails.setSpecializationMajorYear(null);
        deleteStudentMajorDetails.setStudent(null);
        studentMajorDetailsDAO.delete(deleteStudentMajorDetails);

        Optional<StudentMajorDetails> deletedMajorDetails = studentMajorDetailsDAO.findById(studentMajorDetailsId);
        if (deletedMajorDetails.isPresent()) {
            response.put("response", "Failed to remove student major detail");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("response", "Student major details deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<Map<String,String>> editStudentMajorDetails(Student student, Integer studentMajorDetailsId, Integer requestBodyId) {
        Optional<StudentMajorDetails> optionalStudentMajorDetails = studentMajorDetailsDAO.findById(studentMajorDetailsId);
        Map<String, String> response = new HashMap<>();

        if (optionalStudentMajorDetails.isEmpty()) {
            response.put("response", "Major Details not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        if(optionalStudentMajorDetails.get().getStudent().getId() != student.getId()) {
            response.put("response", "Access forbidden");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        StudentMajorDetails updateStudentMajorDetails = optionalStudentMajorDetails.get();
        Optional<SpecializationMajorYear> requestedSpecializationMajorYear = specializationMajorYearFetcher.getSpecializationMajorYear(requestBodyId);

        if (requestedSpecializationMajorYear.isEmpty()) {
            response.put("response", "Specialization Major Year not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        updateStudentMajorDetails.setSpecializationMajorYear(requestedSpecializationMajorYear.get());
        studentMajorDetailsDAO.save(updateStudentMajorDetails);

        Optional<StudentMajorDetails> updatedStudentMajorDetails = studentMajorDetailsDAO.findById(studentMajorDetailsId);
        if (updatedStudentMajorDetails.isPresent() && updatedStudentMajorDetails.get().equals(updateStudentMajorDetails)) {
            response.put("response", "Major details updated successfully");
            return ResponseEntity.ok().body(response);
        } else {
            response.put("response", "Failed to update major details");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public void addStudentMajorDetails(StudentMajorDetails studentMajorDetails) {
        studentMajorDetailsDAO.save(studentMajorDetails);
    }
}
