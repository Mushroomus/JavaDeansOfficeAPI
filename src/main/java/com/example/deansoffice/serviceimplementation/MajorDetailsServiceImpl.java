package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.MajorDetailsMapper;
import com.example.deansoffice.dao.MajorDetailsDAO;
import com.example.deansoffice.dto.MajorDetailsDTO;
import com.example.deansoffice.service.MajorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorDetailsServiceImpl implements MajorDetailsService {
    private final MajorDetailsDAO majorDetailsDAO;

    @Autowired
    private MajorDetailsMapper majorDetailsMapper;

    @Autowired
    public MajorDetailsServiceImpl(MajorDetailsDAO theMajorDetailsDAO) {
        majorDetailsDAO = theMajorDetailsDAO;
    }

    private List<MajorDetailsDTO> getAllMajors() {
        return  majorDetailsMapper.toDTOList(majorDetailsDAO.findAll());
    }


    private List<MajorDetailsDTO> getMajorsByYear(int year) {
        return  majorDetailsMapper.toDTOList(majorDetailsDAO.findAllByYear(year));
    }

    @Override
    public ResponseEntity<Map<String,List<MajorDetailsDTO>>> getAllMajors(Integer year) {
        Map<String,List<MajorDetailsDTO>> result = new HashMap<>(1);

        if(year != null)
            result.put("majors", getMajorsByYear(year));
        else
            result.put("majors", getAllMajors());

        return ResponseEntity.ok().body(result);
    }
}
