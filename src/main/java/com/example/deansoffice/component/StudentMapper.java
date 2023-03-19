package com.example.deansoffice.component;

import com.example.deansoffice.dto.StudentDTO;
import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Student;
import com.example.deansoffice.entity.Worker;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudentMapper {
    private final ModelMapper modelMapper;
    public StudentMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public StudentDTO toDto(Student student) {
        return modelMapper.map(student, StudentDTO.class);
    }

    public Student toEntity(StudentDTO studentDTO) {
        return modelMapper.map(studentDTO, Student.class);
    }

    public List<StudentDTO> toDTOList(List<Student> students) {
        return students.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Student> toEntityList(List<StudentDTO> studentDTOs) {
        return studentDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
