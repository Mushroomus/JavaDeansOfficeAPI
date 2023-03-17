package com.example.deansoffice.service;

import com.example.deansoffice.dao.WorkDateDAO;
import com.example.deansoffice.entity.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkDateServiceImpl implements  WorkDateService {
    private WorkDateDAO workDateDAO;

    @Autowired
    public WorkDateServiceImpl(WorkDateDAO theWorkDateDAO) {
        workDateDAO = theWorkDateDAO;
    }
}
