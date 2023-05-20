package com.example.deansoffice.dto;

import com.example.deansoffice.entity.WorkerSpecialization;

import java.util.List;
import java.util.stream.Collectors;

public record WorkerSpecializationDTO(int id, SpecializationDTO specialization) {
    public WorkerSpecializationDTO(WorkerSpecialization workerSpecialization) {
        this(workerSpecialization.getId(),
                new SpecializationDTO(workerSpecialization.getSpecialization()));
    }
    public static List<WorkerSpecializationDTO> fromEntities(List<WorkerSpecialization> entities) {
        return entities.stream()
                .map(WorkerSpecializationDTO::new)
                .collect(Collectors.toList());
    }
}
