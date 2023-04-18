package com.example.deansoffice.dao;

import com.example.deansoffice.entity.WorkerSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerSpecializationDAO extends JpaRepository<WorkerSpecialization, Integer> {
}
