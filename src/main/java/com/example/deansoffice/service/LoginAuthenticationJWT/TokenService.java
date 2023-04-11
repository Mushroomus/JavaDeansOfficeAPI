package com.example.deansoffice.service.LoginAuthenticationJWT;

import com.example.deansoffice.entity.Token;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface TokenService {
    List<Token> findAllValidTokenByUser(Integer id);
    Optional<Token> findByToken(String token);
    void saveToken(Token token);
    void saveAllTokens(List<Token> tokens);
}
