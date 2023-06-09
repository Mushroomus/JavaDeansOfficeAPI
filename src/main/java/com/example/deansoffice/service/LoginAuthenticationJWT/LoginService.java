package com.example.deansoffice.service.LoginAuthenticationJWT;

import com.example.deansoffice.entity.Login;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface LoginService {
    Optional<Login> findByUsername(String username);
}
