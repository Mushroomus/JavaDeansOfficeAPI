package com.example.deansoffice.dto;

import com.example.deansoffice.entity.MajorDetails;
import com.example.deansoffice.entity.Student;
import lombok.Data;

import java.util.List;

@Data
public class MajorDetailsDTO {
    private int id;
    private String major;
    private String specialization;
    private int year;
}
