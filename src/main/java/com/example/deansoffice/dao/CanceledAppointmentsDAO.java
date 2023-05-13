package com.example.deansoffice.dao;

import com.example.deansoffice.entity.CanceledAppointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CanceledAppointmentsDAO extends JpaRepository<CanceledAppointments, Integer> {
}
