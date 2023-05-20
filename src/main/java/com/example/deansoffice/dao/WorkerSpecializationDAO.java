package com.example.deansoffice.dao;

import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.Worker;
import com.example.deansoffice.entity.WorkerSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface WorkerSpecializationDAO extends JpaRepository<WorkerSpecialization, Integer> {
    List<WorkerSpecialization> findAllByWorkerId(Integer workerId);

    @Query("SELECT CASE WHEN COUNT(ws) > 0 THEN true ELSE false END " +
            "FROM WorkerSpecialization ws WHERE ws.worker = :worker AND ws.specialization = :specialization")
    boolean existsByWorkerAndSpecialization(@Param("worker") Worker worker,
                                            @Param("specialization") Specialization specialization);
}
