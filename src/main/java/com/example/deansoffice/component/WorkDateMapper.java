package com.example.deansoffice.component;

import com.example.deansoffice.dto.WorkDateDTO;
import com.example.deansoffice.entity.WorkDate;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class WorkDateMapper {
    private final ModelMapper modelMapper;
    private final WorkerMapper workerMapper;

    public WorkDateMapper(ModelMapper modelMapper, WorkerMapper workerMapper) {
        this.modelMapper = modelMapper;
        this.workerMapper = workerMapper;
    }

    public WorkDateDTO toDto(WorkDate workDate) {
        WorkDateDTO workDateDTO = modelMapper.map(workDate, WorkDateDTO.class);
        workDateDTO.setWorker(workerMapper.toDto(workDate.getWorker()));
        return workDateDTO;
    }

    public WorkDate toEntity(WorkDateDTO workDateDTO) {
        WorkDate workDate = modelMapper.map(workDateDTO, WorkDate.class);
        workDate.setWorker(workerMapper.toEntity(workDateDTO.getWorker()));
        return workDate;
    }

    public List<WorkDateDTO> toDtoList(List<WorkDate> workDates) {
        return workDates.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WorkDate> toEntityList(List<WorkDateDTO> workDateDTOs) {
        return workDateDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
