package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface StudentDAO extends JpaRepository<Student, Integer> {

    @Query("SELECT DISTINCT smy.specialization.id " +
            "FROM SpecializationMajorYear smy " +
            "INNER JOIN StudentMajorDetails smd " +
            "WHERE smd.student.id = :studentId")
    List<Integer> getStudentSpecializationIdList(@Param("studentId") Integer studentId);
}
