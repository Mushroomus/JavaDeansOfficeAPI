package com.example.deansoffice.dao;

import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.StudentMajorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface StudentMajorDetailsDAO extends JpaRepository<StudentMajorDetails, Integer> {
    List<StudentMajorDetails> findAllByStudent(Student student);
    Optional<StudentMajorDetails> findByStudentAndSpecializationMajorYear(Student student,
                                                                          SpecializationMajorYear specializationMajorYear);
}
