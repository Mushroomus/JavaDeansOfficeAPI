package com.example.deansoffice.dao;

import com.example.deansoffice.entity.CanceledAppointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanceledAppointmentsDAO extends JpaRepository<CanceledAppointments, Integer> {
}
