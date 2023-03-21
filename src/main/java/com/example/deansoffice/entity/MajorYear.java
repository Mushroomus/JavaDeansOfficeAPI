package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "major_year")
public class MajorYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="year")
    private int year;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "specialization_major_year",
            joinColumns = { @JoinColumn(name = "major_year_id") },
            inverseJoinColumns = { @JoinColumn(name = "specialization_id") }
    )
    private List<Specialization> specializations;
}
