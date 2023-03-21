package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Specialization;
import lombok.Data;

import java.util.List;

@Data
public class MajorDetailsDTO {
    private int id;
    private int year;
    private List<Specialization> specializations;
}
