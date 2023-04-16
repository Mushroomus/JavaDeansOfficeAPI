package com.example.deansoffice.service.Fetcher;

import com.example.deansoffice.entity.SpecializationMajorYear;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SpecializationMajorYearFetcher {
    Optional<SpecializationMajorYear> getSpecializationMajorYear(Integer id);
}
