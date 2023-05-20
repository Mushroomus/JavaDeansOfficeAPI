package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Manager.AdminSpecializationManager;
import com.example.deansoffice.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SpecializationServiceImpl implements SpecializationService, AdminSpecializationManager, SpecializationFetcher {
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
    public Optional<Specialization> getSpecializationById(Integer id) {
        return specializatioDAO.findById(id);
    }


    @Override
    public List<SpecializationDTO> getSpecializations() {
        try {
            List<Specialization> entitiesSpecialization = specializatioDAO.findAll();
            return SpecializationDTO.fromEntities(entitiesSpecialization);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get specializations");
        }
    }

    @Override
    public ResponseEntity<Response> addSpecialization(Specialization specialization) {
        try {
            specializatioDAO.save(specialization);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Specialization created"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to create specialization");
        }
    }

    @Override
    public ResponseEntity<Response> deleteSpecialization(Integer specializationId) {
        try {
            Optional<Specialization> deleteSpecialization = specializatioDAO.findById(specializationId);

            if (deleteSpecialization.isPresent()) {
                specializatioDAO.delete(deleteSpecialization.get());
                specializatioDAO.findById(specializationId);
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Specialization deleted"));
            } else {
                throw new RecordNotFoundException("Specialization not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete specialization");
        }
    }

    @Override
    public ResponseEntity<Response> updateSpecialization(Specialization specialization) {
        try {
            Optional<Specialization> specializationOptional = specializatioDAO.findById(specialization.getId());

            if (specializationOptional.isPresent()) {
                Specialization existingSpecialization = specializationOptional.get();
                existingSpecialization.setName(specialization.getName());
                existingSpecialization.setCourse(specialization.getCourse());
                specializatioDAO.save(existingSpecialization);
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Specialization updated"));
            } else {
                throw new RecordNotFoundException("Specialization not found");
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new InternalServerErrorException("Failed to update specialization");
        }
    }
}
