package com.example.deansoffice.dao;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface WorkerDAO extends JpaRepository<Worker, Integer> {
    @Query("SELECT DISTINCT new com.example.deansoffice.dto.WorkerDTO(w.id, w.name, w.surname, w.phoneNumber, w.email, w.room) FROM Worker w INNER JOIN w.specializations s WHERE s.id IN :specializationIds")
    List<WorkerDTO> getWorkersBySpecializations(@Param("specializationIds") List<Integer> studentSpecializationIdList);
}
