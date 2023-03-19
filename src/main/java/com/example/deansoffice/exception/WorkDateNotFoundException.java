package com.example.deansoffice.exception;

public class WorkDateNotFoundException extends RuntimeException {
    public WorkDateNotFoundException() {
        super("Work date not found");
    }
}