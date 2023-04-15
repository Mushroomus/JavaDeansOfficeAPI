package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationDAO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Manager.AdminSpecializationManager;
import com.example.deansoffice.service.SpecializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    public ResponseEntity<Map<String, String>> addSpecialization(Specialization specialization) {
        Specialization savedSpecialization = specializatioDAO.save(specialization);
        Map<String, String> response = new HashMap<>();

        if(savedSpecialization.getId() > 0) {
            response.put("response", "Specialization saved");
            return ResponseEntity.ok(response);
        } else {
            response.put("response", "Failed to save specialization");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSpecialization(Integer specializationId) {
        Optional<Specialization> deleteSpecialization = specializatioDAO.findById(specializationId);
        Map<String,String> response = new HashMap<>();

        if(deleteSpecialization.isPresent()) {
            specializatioDAO.delete(deleteSpecialization.get());
            deleteSpecialization = specializatioDAO.findById(specializationId);
            if(deleteSpecialization.isEmpty()) {
                response.put("response", "Specialization deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("response", "Something went wrong");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("response", "Specialization doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> updateSpecialization(Specialization specialization) {
        Optional<Specialization> specializationOptional = specializatioDAO.findById(specialization.getId());
        Map<String, String> response = new HashMap<>();

        if (specializationOptional.isPresent()) {
            Specialization existingSpecialization = specializationOptional.get();
            existingSpecialization.setName(specialization.getName());
            existingSpecialization.setCourse(specialization.getCourse());
            specializatioDAO.save(existingSpecialization);

            Optional<Specialization> updatedSpecializationOptional = specializatioDAO.findById(specialization.getId());
            if (updatedSpecializationOptional.isPresent()) {
                Specialization updatedSpecialization = updatedSpecializationOptional.get();
                if (updatedSpecialization.getName().equals(specialization.getName())
                        && updatedSpecialization.getCourse().equals(specialization.getCourse())) {
                    response.put("message", "Specialization updated");
                    return ResponseEntity.ok(response);
                }
            }
            response.put("message", "Failed to update specialization");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            response.put("message", "Specialization not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
