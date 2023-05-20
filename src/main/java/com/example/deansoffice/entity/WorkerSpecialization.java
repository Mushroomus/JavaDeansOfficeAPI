package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="worker_specialization")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
