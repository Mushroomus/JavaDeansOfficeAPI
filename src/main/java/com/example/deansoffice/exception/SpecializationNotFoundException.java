package com.example.deansoffice.exception;

public class SpecializationNotFoundException extends RuntimeException {
    public SpecializationNotFoundException() {
        super("Specialization not found");
    }
}