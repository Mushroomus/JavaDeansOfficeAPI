package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.StudentMajorDetailsDAO;
import com.example.deansoffice.dto.StudentMajorDetailsDTO;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import com.example.deansoffice.exception.AccessForbiddenException;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.service.Fetcher.SpecializationMajorYearFetcher;
import com.example.deansoffice.service.Manager.StudentMajorDetailsManager;
import com.example.deansoffice.service.StudentMajorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public ResponseEntity<Response> addStudentMajorDetails(Student student, Integer specializationMajorYearId) {
        try {
            Optional<SpecializationMajorYear> specializationMajorYearOptional = specializationMajorYearFetcher.getSpecializationMajorYear(specializationMajorYearId);

            if (specializationMajorYearOptional.isPresent()) {
                StudentMajorDetails studentMajorDetails = StudentMajorDetails.builder()
                        .student(student)
                        .specializationMajorYear(specializationMajorYearOptional.get())
                        .build();

                studentMajorDetailsDAO.save(studentMajorDetails);
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Major details added to student"));
            } else {
                throw new RecordNotFoundException("Specialization major year not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to add major details to student");
        }
    }

    @Override
    public ResponseEntity<Response> deleteStudentMajorDetails(Student student, Integer majorDetailsId) {
        try {
            Optional<SpecializationMajorYear> majorDetails = specializationMajorYearFetcher.getSpecializationMajorYear(majorDetailsId);

            if(majorDetails.isEmpty()) {
                throw new RecordNotFoundException("Major details not found");
            }

            Optional<StudentMajorDetails> optionalStudentMajorDetails =
                    studentMajorDetailsDAO.findByStudentAndSpecializationMajorYear(student, majorDetails.get());

            if (!optionalStudentMajorDetails.isPresent()) {
                throw new RecordNotFoundException("Student major details not found");
            }

            if (optionalStudentMajorDetails.get().getStudent().getId() != student.getId()) {
                throw new AccessForbiddenException();
            }

            StudentMajorDetails deleteStudentMajorDetails = optionalStudentMajorDetails.get();
            deleteStudentMajorDetails.setSpecializationMajorYear(null);
            deleteStudentMajorDetails.setStudent(null);
            studentMajorDetailsDAO.delete(deleteStudentMajorDetails);

            return ResponseEntity.status(HttpStatus.OK).body(new Response("Major details deleted from student"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete major details from student");
        }
    }

    @Override
    public ResponseEntity<Response> editStudentMajorDetails(Student student, Integer studentMajorDetailsId, Integer requestBodyId) {
        try {
            Optional<StudentMajorDetails> optionalStudentMajorDetails = studentMajorDetailsDAO.findById(studentMajorDetailsId);

            if (optionalStudentMajorDetails.isEmpty()) {
                throw new RecordNotFoundException("Major details not found");
            }

            if (optionalStudentMajorDetails.get().getStudent().getId() != student.getId()) {
                throw new AccessForbiddenException();
            }

            StudentMajorDetails updateStudentMajorDetails = optionalStudentMajorDetails.get();
            Optional<SpecializationMajorYear> requestedSpecializationMajorYear = specializationMajorYearFetcher.getSpecializationMajorYear(requestBodyId);

            if (requestedSpecializationMajorYear.isEmpty()) {
                throw new RecordNotFoundException("Specialization major year not found");
            }

            updateStudentMajorDetails.setSpecializationMajorYear(requestedSpecializationMajorYear.get());
            studentMajorDetailsDAO.save(updateStudentMajorDetails);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Student major details edited"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to edit student major details");
        }
    }

    @Override
    public void addStudentMajorDetails(StudentMajorDetails studentMajorDetails) {
        studentMajorDetailsDAO.save(studentMajorDetails);
    }

    @Override
    public List<StudentMajorDetailsDTO> getStudentMajorDetails(Student student) {
        List<StudentMajorDetails> studentMajorDetailsList = studentMajorDetailsDAO.findAllByStudent(student);
        return StudentMajorDetailsDTO.fromEntities(studentMajorDetailsList);
    }
}
