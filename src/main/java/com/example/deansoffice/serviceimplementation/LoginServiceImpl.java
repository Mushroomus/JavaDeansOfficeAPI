package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.component.MajorDetailsMapper;
import com.example.deansoffice.dao.LoginDAO;
import com.example.deansoffice.dao.MajorDetailsDAO;
import com.example.deansoffice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private LoginDAO loginDAO;
    @Autowired
    public LoginServiceImpl(LoginDAO theLoginDAO) {
        loginDAO = theLoginDAO;
    }
}
