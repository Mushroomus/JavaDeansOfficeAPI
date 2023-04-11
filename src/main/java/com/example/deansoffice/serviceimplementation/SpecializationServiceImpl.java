package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService, SpecializationFetcher {
    private SpecializationDAO specializatioDAO;

    @Autowired
    public SpecializationServiceImpl(SpecializationDAO theSpecializationDAO) {
        specializatioDAO = theSpecializationDAO;
    }

    @Override
    public Specialization findBySpecializationByNameAndCourse(String name, String course) {
        return specializatioDAO.findByNameAndCourse(name, course);
    }

    @Override
    public Optional<Specialization> getSpecializationById(int id) {
        return specializatioDAO.findById(id);
    }
}
