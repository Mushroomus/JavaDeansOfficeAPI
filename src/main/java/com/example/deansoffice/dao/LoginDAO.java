package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface LoginDAO extends JpaRepository<Login, Integer> {
    Optional<Login> findByUsername(String username);
}
