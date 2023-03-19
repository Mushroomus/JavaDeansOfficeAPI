package com.example.deansoffice.dao;

import com.example.deansoffice.entity.WorkDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDateDAO extends JpaRepository<WorkDate, Integer> {
}
