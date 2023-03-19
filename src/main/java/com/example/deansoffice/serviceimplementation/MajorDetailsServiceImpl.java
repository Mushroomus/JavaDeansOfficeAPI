package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.MajorDetailsMapper;
import com.example.deansoffice.dao.MajorDetailsDAO;
import com.example.deansoffice.service.MajorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MajorDetailsServiceImpl implements MajorDetailsService {
    private MajorDetailsDAO majorDetailsDAO;
    @Autowired
    private MajorDetailsMapper majorDetailsMapper;

    @Autowired
    public MajorDetailsServiceImpl(MajorDetailsDAO theMajorDetailsDAO) {
        majorDetailsDAO = theMajorDetailsDAO;
    }
}
