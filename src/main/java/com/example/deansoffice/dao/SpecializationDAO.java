package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface SpecializationDAO extends JpaRepository<Specialization, Integer> {
    Specialization findByNameAndCourse(String name, String course);
}
