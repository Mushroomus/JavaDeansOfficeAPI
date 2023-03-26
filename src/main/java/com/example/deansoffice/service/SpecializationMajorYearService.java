package com.example.deansoffice.service;

import com.example.deansoffice.dao.SpecializationMajorYearDAO;
import com.example.deansoffice.entity.SpecializationMajorYear;
import org.springframework.stereotype.Service;

@Service
public interface SpecializationMajorYearService {
    SpecializationMajorYear findSpecializationMajorYearByMajorYearIdAndSpecializationId(int studentId, int specializationMajorYearId);
}
