package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.MajorYearDAO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.service.Fetcher.MajorYearFetcher;
import com.example.deansoffice.service.Manager.AdminMajorYearManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MajorYearImpl implements AdminMajorYearManager, MajorYearFetcher {
    MajorYearDAO majorYearDAO;
    MajorYearImpl(MajorYearDAO theMajorYearDAO) {
        majorYearDAO = theMajorYearDAO;
    }

    @Override
    public ResponseEntity<Map<String, String>> addMajorYear(Integer year) {
        MajorYear majorYear = MajorYear.builder()
                .year(year)
                .build();
        MajorYear savedMajorYear = majorYearDAO.save(majorYear);

        Map<String,String> response = new HashMap<>();

        if (savedMajorYear.getId() > 0) {
            response.put("message", "Year added");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Failed to add year");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteMajorYear(Integer id) {
        Optional<MajorYear> deleteMajorYear = majorYearDAO.findById(id);
        Map<String,String> response = new HashMap<>();

        if(deleteMajorYear.isPresent()) {
            majorYearDAO.delete(deleteMajorYear.get());
            deleteMajorYear = majorYearDAO.findById(id);
            if(deleteMajorYear.isEmpty()) {
                response.put("response", "Year deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("response", "Something went wrong");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("response", "Year doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    public Optional<MajorYear> getMajorYearById(Integer id) {
        return majorYearDAO.findById(id);
    }
}
