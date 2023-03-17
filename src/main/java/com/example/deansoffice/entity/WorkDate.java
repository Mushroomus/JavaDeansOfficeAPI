package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="work_date")
@Data
public class WorkDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name="date")
    LocalDate date;

    @Column(name="start_time")
    LocalTime start_time;

    @Column(name="end_time")
    LocalTime end_time;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;
}
