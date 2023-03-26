package com.example.deansoffice.dao;

import com.example.deansoffice.entity.SpecializationMajorYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationMajorYearDAO extends JpaRepository<SpecializationMajorYear, Integer> {
    SpecializationMajorYear findSpecializationMajorYearByMajorYearIdAndSpecializationId(int student_id, int specialization_major_year_id);
}
