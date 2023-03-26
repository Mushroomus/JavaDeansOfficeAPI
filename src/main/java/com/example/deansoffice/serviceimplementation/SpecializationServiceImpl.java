package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecializationServiceImpl implements SpecializationService {
    private SpecializationDAO specializatioDAO;

    @Autowired
    public SpecializationServiceImpl(SpecializationDAO theSpecializationDAO) {
        specializatioDAO = theSpecializationDAO;
    }

    @Override
    public Specialization findBySpecializationByNameAndCourse(String name, String course) {
        return specializatioDAO.findByNameAndCourse(name, course);
    }
}
