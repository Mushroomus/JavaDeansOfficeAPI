package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.LoginDAO;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.entity.Token;
import com.example.deansoffice.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private LoginDAO loginDAO;
    @Autowired
    public LoginServiceImpl(LoginDAO theLoginDAO) {
        loginDAO = theLoginDAO;
    }

    @Override
    public Optional<Login> findByUsername(String username) {
        return loginDAO.findByUsername(username);
    }
}
