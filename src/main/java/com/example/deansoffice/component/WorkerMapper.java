package com.example.deansoffice.component;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class WorkerMapper {
    private final ModelMapper modelMapper;

    public WorkerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public WorkerDTO toDto(Worker worker) {
        return modelMapper.map(worker, WorkerDTO.class);
    }

    public Worker toEntity(WorkerDTO workerDTO) {
        return modelMapper.map(workerDTO, Worker.class);
    }

    public List<WorkerDTO> toDTOList(List<Worker> workers) {
        return workers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Worker> toEntityList(List<WorkerDTO> workerDTOs) {
        return workerDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
