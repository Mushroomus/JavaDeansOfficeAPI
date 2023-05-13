package com.example.deansoffice.dao;

import java.util.List;
import java.util.Optional;

import com.example.deansoffice.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(exported = false)
public interface TokenDAO extends JpaRepository<Token, Integer> {

  @Query("select t from Token t inner join Login u on t.login.id = u.id where u.id = :id and (t.expired = false or t.revoked = false)")
  List<Token> findAllValidTokenByUser(Integer id);

  Optional<Token> findByToken(String token);
}