package com.example.deansoffice.service;

import com.example.deansoffice.entity.Specialization;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public interface SpecializationService {
    Specialization findBySpecializationByNameAndCourse(String name, String course);
}
