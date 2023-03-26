package com.example.deansoffice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="login_student")
@Data
public class Login {
    @Id
    private int id;
    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Student student;
}
