package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "specialization_major_year")
public class SpecializationMajorYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private Specialization specialization;

    @ManyToOne
    @JoinColumn(name = "major_year_id", referencedColumnName = "id")
    private MajorYear majorYear;
}
