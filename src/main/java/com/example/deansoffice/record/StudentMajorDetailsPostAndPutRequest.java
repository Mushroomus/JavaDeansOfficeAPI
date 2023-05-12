package com.example.deansoffice.record;

import java.util.Objects;

public record StudentMajorDetailsPostAndPutRequest(Integer specializationMajorYearId) {
    public StudentMajorDetailsPostAndPutRequest {
        Objects.requireNonNull(specializationMajorYearId, "Specialization major year id cannot be null");
    }
}