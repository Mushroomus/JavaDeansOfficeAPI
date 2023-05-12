package com.example.deansoffice.model;

import java.util.Objects;

public record SpecializationMajorYearPostRequest(Integer specialization, Integer majorYear) {
    public SpecializationMajorYearPostRequest {
        Objects.requireNonNull(specialization, "specialization must not be null");
        Objects.requireNonNull(majorYear, "majorYear must not be null");
    }
}