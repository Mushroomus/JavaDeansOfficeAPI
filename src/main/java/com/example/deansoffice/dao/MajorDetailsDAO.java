package com.example.deansoffice.dao;

import com.example.deansoffice.entity.MajorDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorDetailsDAO extends JpaRepository<MajorDetails, Integer> {
}
