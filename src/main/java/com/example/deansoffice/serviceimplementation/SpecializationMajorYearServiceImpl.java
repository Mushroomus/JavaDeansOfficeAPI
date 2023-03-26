package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.dao.SpecializationMajorYearDAO;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.service.SpecializationMajorYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecializationMajorYearServiceImpl implements SpecializationMajorYearService {

    private SpecializationMajorYearDAO specializationMajorYearDAO;
    @Autowired
    public SpecializationMajorYearServiceImpl(SpecializationMajorYearDAO theSpecializationMajorYearDAO) {
        specializationMajorYearDAO = theSpecializationMajorYearDAO;
    }

    public SpecializationMajorYear findSpecializationMajorYearByMajorYearIdAndSpecializationId(int studentId, int specializationMajorYearId) {
        return specializationMajorYearDAO.findSpecializationMajorYearByMajorYearIdAndSpecializationId(studentId, specializationMajorYearId);
    }

}
