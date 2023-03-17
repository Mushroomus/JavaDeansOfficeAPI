package com.example.deansoffice.dto;

import com.example.deansoffice.entity.Worker;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WorkDateDTO {
    int id;
    LocalDate date;
    LocalTime start_time;
    LocalTime end_time;
    private WorkerDTO worker;
}
