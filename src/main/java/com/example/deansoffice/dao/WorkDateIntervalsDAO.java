package com.example.deansoffice.dao;

import com.example.deansoffice.entity.WorkDate;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.model.Pair;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public interface WorkDateIntervalsDAO extends JpaRepository<WorkDateIntervals, Integer> {

    @Query("SELECT wd.date, wdi.startInterval, wdi.endInterval, wdi.description, w.name, w.surname " +
            "FROM WorkDateIntervals wdi " +
            "JOIN wdi.workDate wd " +
            "JOIN wd.worker w " +
            "WHERE wdi.student.id = :studentId " +
            "AND (:startInterval IS NULL OR wdi.startInterval >= :startInterval) " +
            "AND (:endInterval IS NULL OR wdi.endInterval <= :endInterval) " +
            "AND (:startDate IS NULL OR wd.date >= :startDate) " +
            "AND (:endDate IS NULL OR wd.date <= :endDate) " +
            "AND (:workerId IS NULL OR w.id = :workerId)")
            List<Object[]> findByStudentIdAndStartInvervalAndEndInterval(
            @Param("studentId") int studentId,
            @Param("startInterval") LocalTime startInterval,
            @Param("endInterval") LocalTime endInterval,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("workerId") Integer workerId
            );

    @Query("SELECT NEW com.example.deansoffice.model.Pair(wdi.startInterval, wdi.endInterval) " +
            "FROM WorkDateIntervals wdi " +
            "JOIN wdi.workDate wd " +
            "JOIN wd.worker w " +
            "WHERE w.id = :workerId " +
            "AND wd.id = :workDateId")
    List<Pair<LocalTime, LocalTime>> getIntervalsByWorkerIdAndDate(@Param("workerId") int workerId, @Param("workDateId") int workDateId);
}
