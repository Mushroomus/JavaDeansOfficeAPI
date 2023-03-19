package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "student_major_details",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "major_details_id") }
    )
    private List<MajorDetails> majorDetailsList;

    @OneToMany(mappedBy = "student")
    private List<WorkDateIntervals> appointments;
}
