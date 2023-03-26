package com.example.deansoffice.model;

import lombok.Data;

@Data
public class RegisterStudent {
    private String name;
    private String surname;
    private String major;
    private String specialization;
    private int year;
    private String username;
    private String password;

    public void setYear(String year) {
        this.year = Integer.parseInt(year);
    }
}
