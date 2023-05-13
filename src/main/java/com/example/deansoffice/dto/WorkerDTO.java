package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Worker;

import java.util.List;
import java.util.stream.Collectors;

public record WorkerDTO(int id, String name, String surname, String phoneNumber, String email, String room) {
    public WorkerDTO(Worker worker) {
        this(worker.getId(), worker.getName(), worker.getSurname(), worker.getPhoneNumber(), worker.getEmail(), worker.getRoom());
    }

    public static List<WorkerDTO> fromEntities(List<Worker> entities) {
        return entities.stream()
                .map(WorkerDTO::new)
                .collect(Collectors.toList());
    }

    public static Worker toEntity(WorkerDTO dto) {
        return Worker.builder()
                .name(dto.name)
                .surname(dto.surname)
                .phoneNumber(dto.phoneNumber)
                .email(dto.email)
                .room(dto.room)
                .build();
    }
}