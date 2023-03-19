package com.example.deansoffice.dto;

import com.example.deansoffice.entity.MajorDetails;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
    int id;
    private String email;
    private String password;
    private String name;
    private String surname;
    private Boolean isActive;
    private List<MajorDetails> majorDetailsList;
    private List<WorkDateIntervals> appointments;
}
