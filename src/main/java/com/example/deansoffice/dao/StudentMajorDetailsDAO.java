package com.example.deansoffice.dao;

import com.example.deansoffice.entity.StudentMajorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMajorDetailsDAO extends JpaRepository<StudentMajorDetails, Integer> {
}
