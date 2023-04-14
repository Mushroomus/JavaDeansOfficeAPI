package com.example.deansoffice.dao;

import com.example.deansoffice.entity.MajorYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorYearDAO extends JpaRepository<MajorYear, Integer> {
}
