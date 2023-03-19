package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "major_details")
public class MajorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "major")
    private String major;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "year")
    private int year;
}
