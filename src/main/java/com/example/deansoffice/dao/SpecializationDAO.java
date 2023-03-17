package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecializationDAO extends JpaRepository<Specialization, Integer> {
}
