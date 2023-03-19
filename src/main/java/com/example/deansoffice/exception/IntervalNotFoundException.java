package com.example.deansoffice.exception;

public class IntervalNotFoundException extends RuntimeException {
    public IntervalNotFoundException() {
        super("Interval not found");
    }
}