package com.example.deansoffice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationMajorYearPostRequest {
    private Integer specialization;
    private Integer majorYear;
}
