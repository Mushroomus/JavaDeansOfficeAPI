package com.example.deansoffice.service.Manager;

import com.example.deansoffice.entity.Login;
import org.springframework.stereotype.Service;

@Service
public interface AdminLoginManager {
    void saveLogin(Login workerLogin);
}
