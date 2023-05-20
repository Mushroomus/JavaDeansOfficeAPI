package com.example.deansoffice.dao;

import com.example.deansoffice.entity.WorkDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(exported = false)
public interface WorkDateDAO extends JpaRepository<WorkDate, Integer> {
    @Query("SELECT w.date FROM WorkDate w WHERE w.date > CURRENT_DATE() AND w.worker.id = :workerId")
    List<LocalDate> getWorkDatesGreaterThanCurrentDate(@Param("workerId") int workerId);
}
