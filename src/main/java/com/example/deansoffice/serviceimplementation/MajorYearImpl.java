package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.MajorYearDAO;
import com.example.deansoffice.dto.MajorYearDTO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.exception.RecordNotFoundException;
import com.example.deansoffice.model.Response;
import com.example.deansoffice.service.Fetcher.MajorYearFetcher;
import com.example.deansoffice.service.Manager.AdminMajorYearManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class MajorYearImpl implements AdminMajorYearManager, MajorYearFetcher {
    MajorYearDAO majorYearDAO;
    MajorYearImpl(MajorYearDAO theMajorYearDAO) {
        majorYearDAO = theMajorYearDAO;
    }


    @Override
    public List<MajorYearDTO> getMajorYears() {
        try {
            List<MajorYear> majorYears = majorYearDAO.findAll();
            return MajorYearDTO.fromEntities(majorYears);
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to get major years");
        }
    }

    @Override
    public ResponseEntity<Response> addMajorYear(Integer year) {
        try {
            MajorYear majorYear = MajorYear.builder()
                    .year(year)
                    .build();
            majorYearDAO.save(majorYear);
            return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Major year added"));
        } catch(Exception e) {
            throw new InternalServerErrorException("Failed to add year");
        }
    }

    @Override
    public ResponseEntity<Response> deleteMajorYear(Integer id) {
        try {
            Optional<MajorYear> deleteMajorYear = majorYearDAO.findById(id);
            if(deleteMajorYear.isPresent()) {
                majorYearDAO.delete(deleteMajorYear.get());
                return ResponseEntity.status(HttpStatus.OK).body(new Response("Major year deleted"));
            } else {
                throw new RecordNotFoundException("Major year not found");
            }
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to delete major year");
        }
    }

    @Override
    public Optional<MajorYear> getMajorYearById(Integer id) {
        return majorYearDAO.findById(id);
    }
}
