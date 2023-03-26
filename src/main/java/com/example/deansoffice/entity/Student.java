package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "student")
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "student_major_details",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "specialization_major_year_id")}
    )
    private List<SpecializationMajorYear> specializationMajorYears;

    @OneToMany(mappedBy = "student")
    private List<WorkDateIntervals> appointments;
}
