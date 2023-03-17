package com.example.deansoffice.component;

import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.entity.Specialization;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class SpecializationMapper {
    private final ModelMapper modelMapper;

    public SpecializationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SpecializationDTO toDTO(Specialization specialization) {
        return modelMapper.map(specialization, SpecializationDTO.class);
    }

    public Specialization toEntity(SpecializationDTO specializationDTO) {
        return modelMapper.map(specializationDTO, Specialization.class);
    }

    public List<SpecializationDTO> toDTOList(List<Specialization> specializations) {
        return specializations.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Specialization> toEntityList(List<SpecializationDTO> specializationDTOs) {
        return specializationDTOs.stream().map(this::toEntity).collect(Collectors.toList());
    }
}
