package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="worker")
@Data
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="email")
    private String email;

    @Column(name="room")
    private String room;

    @OneToMany
    @JoinColumn(name = "worker_id")
    private List<WorkDate> workDates;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "worker_specialization",
            joinColumns = { @JoinColumn(name = "worker_id") },
            inverseJoinColumns = { @JoinColumn(name = "specialization_id") }
    )
    private List<Specialization> specializations;

}
