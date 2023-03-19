package com.example.deansoffice.component;

import com.example.deansoffice.dao.MajorDetailsDAO;
import com.example.deansoffice.dto.MajorDetailsDTO;
import com.example.deansoffice.entity.MajorDetails;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MajorDetailsMapper {
    private final ModelMapper modelMapper;
    public MajorDetailsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MajorDetailsDTO toDto(MajorDetails majorDetails) {
        return modelMapper.map(majorDetails, MajorDetailsDTO.class);
    }

    public MajorDetails toEntity(MajorDetailsDAO majorDetailsDAO) {
        return modelMapper.map(majorDetailsDAO, MajorDetails.class);
    }

    public List<MajorDetailsDTO> toDTOList(List<MajorDetails> majorDetails) {
        return majorDetails.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

/*
    public List<MajorDetails> toEntityList(List<MajorDetailsDTO> majorDetailsDTOs) {
        return majorDetailsDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
 */
}
