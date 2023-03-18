package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.WorkDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkerDTO {
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String room;
    private List<WorkDate> workDates;
    private List<Specialization> specializations;

}
