package com.example.deansoffice.service.Fetcher;

import com.example.deansoffice.entity.MajorYear;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MajorYearFetcher {
    Optional<MajorYear> getMajorYearById(Integer id);
}
