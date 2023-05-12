package com.example.deansoffice.record;

import com.example.deansoffice.exception.InternalServerErrorException;
import com.example.deansoffice.model.Pair;

import java.util.List;
import java.util.Objects;

public record WorkerIntervalsToWorkDayPostRequest(List<Pair<String, String>> intervals) {
    public WorkerIntervalsToWorkDayPostRequest {
        Objects.requireNonNull(intervals, "Interval list cannot be null.");
        if (intervals.isEmpty()) {
            throw new InternalServerErrorException("Interval list cannot be empty");
        }
    }
}