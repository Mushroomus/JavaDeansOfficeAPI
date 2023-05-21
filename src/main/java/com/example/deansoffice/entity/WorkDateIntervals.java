package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="work_date_intervals")
@Data
public class WorkDateIntervals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="start_interval")
    private LocalTime startInterval;

    @Column(name="end_interval")
    private LocalTime endInterval;

    @Column(name="taken")
    private Boolean taken;

    @Column(name="description")
    private String description;

    @ManyToOne(cascade = { CascadeType.REMOVE })
    @JoinColumn(name = "work_date_id", referencedColumnName = "id")
    private WorkDate workDate;

    @ManyToOne(cascade = { CascadeType.REMOVE })
    @JoinColumn(name = "student_id", referencedColumnName = "id")
    private Student student;
}
