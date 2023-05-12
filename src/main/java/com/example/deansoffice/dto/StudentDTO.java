package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Student;

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
}
