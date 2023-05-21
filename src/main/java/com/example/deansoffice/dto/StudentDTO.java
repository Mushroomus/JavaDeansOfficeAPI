package com.example.deansoffice.dto;

import com.example.deansoffice.entity.SpecializationMajorYear;
import com.example.deansoffice.entity.Student;

import java.util.List;
import java.util.stream.Collectors;

public record StudentDTO(int id, String name, String surname) {
    public StudentDTO(Student student) {
        this(student.getId(), student.getName(), student.getSurname());
    }
    public static StudentDTO fromEntity(Student entity) {
        return new StudentDTO(entity.getId(), entity.getName(), entity.getSurname());
    }

    public static Student toEntity(StudentDTO dto) {
        return Student.builder()
                .name(dto.name)
                .surname(dto.surname)
                .build();
    }

    public static List<StudentDTO> fromEntities(List<Student> entities) {
        return entities.stream()
                .map(StudentDTO::new)
                .collect(Collectors.toList());
    }
}
