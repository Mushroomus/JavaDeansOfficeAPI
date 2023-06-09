package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.LoginDAO;
import com.example.deansoffice.entity.Login;
import com.example.deansoffice.service.LoginAuthenticationJWT.LoginService;
import com.example.deansoffice.service.Manager.AdminLoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService, AdminLoginManager {
    private LoginDAO loginDAO;
    @Autowired
    public LoginServiceImpl(LoginDAO theLoginDAO) {
        loginDAO = theLoginDAO;
    }

    @Override
    public Optional<Login> findByUsername(String username) {
        return loginDAO.findByUsername(username);
    }

    @Override
    public void saveLogin(Login workerLogin) {
        loginDAO.save(workerLogin);
    }
}
