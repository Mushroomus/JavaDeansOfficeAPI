package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_major_details")
@Data
public class StudentMajorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "specialization_major_year_id")
    private SpecializationMajorYear specializationMajorYear;
}
