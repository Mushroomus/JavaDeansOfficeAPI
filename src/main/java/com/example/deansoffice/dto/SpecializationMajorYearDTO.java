package com.example.deansoffice.dto;

import com.example.deansoffice.entity.SpecializationMajorYear;

import java.util.List;
import java.util.stream.Collectors;

public record SpecializationMajorYearDTO(int id, SpecializationDTO specialization, MajorYearDTO majorYear) {

    public SpecializationMajorYearDTO(SpecializationMajorYear specializationMajorYear) {
        this(specializationMajorYear.getId(),
             new SpecializationDTO(specializationMajorYear.getSpecialization()),
             new MajorYearDTO(specializationMajorYear.getMajorYear()));
    }

    public static List<SpecializationMajorYearDTO> fromEntities(List<SpecializationMajorYear> entities) {
        return entities.stream()
                .map(SpecializationMajorYearDTO::new)
                .collect(Collectors.toList());
    }
}