package com.example.deansoffice.serviceimplementation;

import com.example.deansoffice.dao.TokenDAO;
import com.example.deansoffice.entity.Token;
import com.example.deansoffice.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {
    private TokenDAO tokenDAO;

    TokenServiceImpl(TokenDAO theTokenDAO) {
        tokenDAO = theTokenDAO;
    }

    @Override
    public List<Token> findAllValidTokenByUser(Integer id) {
        return tokenDAO.findAllValidTokenByUser(id);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenDAO.findByToken(token);
    }

    public void saveToken(Token token) {
        tokenDAO.save(token);
    }

    public void saveAllTokens(List<Token> tokens) {
        tokenDAO.saveAll(tokens);
    }
}
