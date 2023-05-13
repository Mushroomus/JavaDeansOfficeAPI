package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationMajorYearDAO;
import com.example.deansoffice.dto.SpecializationMajorYearDTO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.record.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.Fetcher.MajorYearFetcher;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Fetcher.SpecializationMajorYearFetcher;
import com.example.deansoffice.service.Manager.AdminSpecializationMajorYearManager;
import com.example.deansoffice.service.SpecializationMajorYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecializationMajorYearServiceImpl implements SpecializationMajorYearService, AdminSpecializationMajorYearManager, SpecializationMajorYearFetcher {

    private SpecializationMajorYearDAO specializationMajorYearDAO;
    private MajorYearFetcher majorYearFetcher;
    private SpecializationFetcher specializationFetcher;

    @Autowired
    public SpecializationMajorYearServiceImpl(SpecializationMajorYearDAO theSpecializationMajorYearDAO, MajorYearFetcher theMajorYearFetcher, SpecializationFetcher theSpecializationFetcher) {
        specializationMajorYearDAO = theSpecializationMajorYearDAO;
        majorYearFetcher = theMajorYearFetcher;
        specializationFetcher = theSpecializationFetcher;
    }


    public SpecializationMajorYear findSpecializationMajorYearByMajorYearIdAndSpecializationId(int studentId, int specializationMajorYearId) {
        return specializationMajorYearDAO.findSpecializationMajorYearByMajorYearIdAndSpecializationId(studentId, specializationMajorYearId);
    }


    @Override
    public List<SpecializationMajorYearDTO> getSpecializationMajorYear() {
        try {
            List<SpecializationMajorYear> specializationMajorYearEntities = specializationMajorYearDAO.findAll();
            return SpecializationMajorYearDTO.fromEntities(specializationMajorYearEntities);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get specialization major years");
        }
    }

    @Override
    public ResponseEntity<Response> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        try {
            Optional<MajorYear> majorYearOptional = majorYearFetcher.getMajorYearById(specializationMajorYearPostRequest.majorYear());

            if (majorYearOptional.isEmpty()) {
                throw new RecordNotFoundException("Year not found");
            }

            Optional<Specialization> specializationOptional = specializationFetcher.getSpecializationById(specializationMajorYearPostRequest.specialization());

            if (specializationOptional.isEmpty()) {
                throw new RecordNotFoundException("Specialization not found");
            }

            SpecializationMajorYear addSpecializationMajorYear = SpecializationMajorYear.builder()
                    .majorYear(majorYearOptional.get())
                    .specialization(specializationOptional.get())
                    .build();

            specializationMajorYearDAO.save(addSpecializationMajorYear);
            return ResponseEntity.status(HttpStatus.OK).body(new Response("Specialization major year added"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to add specialization major year");
        }
    }

    @Override
    public ResponseEntity<Response> deleteSpecializationMajorYear(Integer specializationMajorYearId) {
        try {
            Optional<SpecializationMajorYear> deleteSpecializationMajorYear = specializationMajorYearDAO.findById(specializationMajorYearId);

            if (deleteSpecializationMajorYear.isPresent()) {
                specializationMajorYearDAO.delete(deleteSpecializationMajorYear.get());
                specializationMajorYearDAO.findById(specializationMajorYearId);
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Specialization major year deleted"));
            } else {
                throw new RecordNotFoundException("Specialization major year not found");
            }
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to delete specialization major year");
        }
    }

    @Override
    public Optional<SpecializationMajorYear> getSpecializationMajorYear(Integer id) {
        return specializationMajorYearDAO.findById(id);
    }
}
