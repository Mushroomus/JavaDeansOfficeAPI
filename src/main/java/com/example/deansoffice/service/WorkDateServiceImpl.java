package com.example.deansoffice.service;

import com.example.deansoffice.dao.WorkDateDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkDateServiceImpl implements  WorkDateService {
    private WorkDateDAO workDateDAO;

    @Autowired
    public WorkDateServiceImpl(WorkDateDAO theWorkDateDAO) {
        workDateDAO = theWorkDateDAO;
    }
}
