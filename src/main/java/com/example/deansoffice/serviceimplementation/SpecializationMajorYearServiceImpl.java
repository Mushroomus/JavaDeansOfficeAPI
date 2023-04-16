package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.SpecializationMajorYearDAO;
import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.model.SpecializationMajorYearPostRequest;
import com.example.deansoffice.service.Fetcher.MajorYearFetcher;
import com.example.deansoffice.service.Fetcher.SpecializationFetcher;
import com.example.deansoffice.service.Fetcher.SpecializationMajorYearFetcher;
import com.example.deansoffice.service.Manager.AdminSpecializationMajorYearManager;
import com.example.deansoffice.service.SpecializationMajorYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String, String>> addSpecializationMajorYear(SpecializationMajorYearPostRequest specializationMajorYearPostRequest) {
        Optional<MajorYear> majorYearOptional = majorYearFetcher.getMajorYearById(specializationMajorYearPostRequest.getMajorYear());
        Map<String,String> response = new HashMap<>();

        if(majorYearOptional.isEmpty()) {
            response.put("response", "Year not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Optional<Specialization> specializationOptional = specializationFetcher.getSpecializationById(specializationMajorYearPostRequest.getSpecialization());

        if(specializationOptional.isEmpty()) {
            response.put("response", "Specialization not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        SpecializationMajorYear addSpecializationMajorYear = SpecializationMajorYear.builder()
                .majorYear(majorYearOptional.get())
                .specialization(specializationOptional.get())
                .build();

        SpecializationMajorYear savedSpecializationMajorYear = specializationMajorYearDAO.save(addSpecializationMajorYear);

        if(savedSpecializationMajorYear.getId() > 0) {
            response.put("response", "Specialization major year saved");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("response", "Something went wrong");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Override
    public ResponseEntity<Map<String, String>> deleteSpecializationMajorYear(Integer specializationMajorYearId) {
        Optional<SpecializationMajorYear> deleteSpecializationMajorYear = specializationMajorYearDAO.findById(specializationMajorYearId);
        Map<String,String> response = new HashMap<>();

        if(deleteSpecializationMajorYear.isPresent()) {
            specializationMajorYearDAO.delete(deleteSpecializationMajorYear.get());
            deleteSpecializationMajorYear = specializationMajorYearDAO.findById(specializationMajorYearId);
            if(deleteSpecializationMajorYear.isEmpty()) {
                response.put("response", "Specialization Major Year deleted");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response.put("response", "Something went wrong");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } else {
            response.put("response", "Specialization with that Year doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Override
    public Optional<SpecializationMajorYear> getSpecializationMajorYear(Integer id) {
        return specializationMajorYearDAO.findById(id);
    }
}
