package com.example.deansoffice.dto;

import com.example.deansoffice.entity.StudentMajorDetails;

import java.util.List;
import java.util.stream.Collectors;

public record StudentMajorDetailsDTO(int id, StudentDTO student, SpecializationMajorYearDTO specializationMajorYear) {
    public StudentMajorDetailsDTO(StudentMajorDetails studentMajorDetails) {
        this(studentMajorDetails.getId(), new StudentDTO(studentMajorDetails.getStudent()), new SpecializationMajorYearDTO(studentMajorDetails.getSpecializationMajorYear()));
    }
    public static List<StudentMajorDetailsDTO> fromEntities(List<StudentMajorDetails> entities) {
        return entities.stream()
                .map(StudentMajorDetailsDTO::new)
                .collect(Collectors.toList());
    }
}
