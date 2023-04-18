package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="worker_specialization")
@Data
public class WorkerSpecialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "worker_id")
    private Worker worker;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;
}
