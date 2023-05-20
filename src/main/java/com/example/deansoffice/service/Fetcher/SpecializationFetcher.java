package com.example.deansoffice.service.Fetcher;

import com.example.deansoffice.dto.SpecializationDTO;
import com.example.deansoffice.entity.Specialization;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SpecializationFetcher {
    Optional<Specialization> getSpecializationById(Integer id);
    List<SpecializationDTO> getSpecializations();
}
