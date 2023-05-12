package com.example.deansoffice.dto;

import com.example.deansoffice.entity.MajorYear;

import java.util.List;
import java.util.stream.Collectors;

public record MajorYearDTO(int id, int year) {
    public MajorYearDTO(MajorYear majorYear) {
        this(majorYear.getId(), majorYear.getYear());
    }

    public static List<MajorYearDTO> fromEntities(List<MajorYear> entities) {
        return entities.stream()
                .map(MajorYearDTO::new)
                .collect(Collectors.toList());
    }

    public static MajorYear toEntity(MajorYearDTO dto) {
        return MajorYear.builder()
                .year(dto.year)
                .build();
    }
}