package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerDAO extends JpaRepository<Worker, Integer> {
}
