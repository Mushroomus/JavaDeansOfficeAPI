package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Specialization;
import com.example.deansoffice.entity.WorkDate;
import lombok.Data;

import java.util.List;

@Data
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
