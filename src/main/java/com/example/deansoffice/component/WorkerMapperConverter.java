package com.example.deansoffice.component;

import com.example.deansoffice.dto.WorkerDTO;
import com.example.deansoffice.entity.Worker;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.modelmapper.Converter;

@Component
public class WorkerMapperConverter implements Converter<Worker, WorkerDTO> {
    @Override
    public WorkerDTO convert(MappingContext<Worker, WorkerDTO> context) {
        Worker worker = context.getSource();
        WorkerDTO workerDTO = new WorkerDTO();

        workerDTO.setId(worker.getId());
        workerDTO.setName(worker.getName());
        workerDTO.setSurname(worker.getSurname());
        workerDTO.setPhoneNumber(worker.getPhoneNumber());
        workerDTO.setEmail(worker.getEmail());
        workerDTO.setRoom(worker.getRoom());
        workerDTO.setSpecializations(worker.getSpecializations());

        return workerDTO;
    }
}