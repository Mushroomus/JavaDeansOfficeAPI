package com.example.deansoffice.dao;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkDateDAO extends JpaRepository<WorkDate, Integer> {
    @Query("SELECT DISTINCT DAY(date) FROM WorkDate s WHERE YEAR(date) = :year AND MONTH(date) = :month AND worker = :worker")
    List<Integer> getDays(@Param("year") int year, @Param("month") int month, @Param("worker") Worker worker);
}
