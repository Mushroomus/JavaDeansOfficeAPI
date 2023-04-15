package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "specialization_major_year")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecializationMajorYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    private Specialization specialization;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "major_year_id", referencedColumnName = "id")
    private MajorYear majorYear;
}
