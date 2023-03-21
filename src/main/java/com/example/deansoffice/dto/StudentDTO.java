package com.example.deansoffice.dto;

import com.example.deansoffice.entity.MajorYear;
import com.example.deansoffice.entity.WorkDateIntervals;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
    private List<MajorYear> majorYearList;
    private List<WorkDateIntervals> appointments;
}
