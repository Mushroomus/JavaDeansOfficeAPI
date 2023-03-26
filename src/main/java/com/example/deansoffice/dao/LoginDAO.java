package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginDAO extends JpaRepository<Login, Integer> {
}
