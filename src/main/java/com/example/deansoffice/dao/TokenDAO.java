package com.example.deansoffice.dao;

import java.util.List;
import java.util.Optional;

import com.example.deansoffice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDAO extends JpaRepository<Token, Integer> {

  @Query(value = """
      select t from Token t inner join Login u\s
      on t.login.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);
}