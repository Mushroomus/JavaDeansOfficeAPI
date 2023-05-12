package com.example.deansoffice.dto;

import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.Specialization;

import java.util.List;
import java.util.stream.Collectors;

public record SpecializationDTO(int id, String name, String course) {
    public SpecializationDTO(Specialization specialization) {
        this(specialization.getId(), specialization.getName(), specialization.getCourse());
    }

    public static List<SpecializationDTO> fromEntities(List<Specialization> entities) {
        return entities.stream()
                .map(SpecializationDTO::new)
                .collect(Collectors.toList());
    }

    public static Specialization toEntity(SpecializationDTO dto) {
        return Specialization.builder()
                .name(dto.name)
                .course(dto.course)
                .build();
    }
}