package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface WorkerDAO extends JpaRepository<Worker, Integer> {
}
