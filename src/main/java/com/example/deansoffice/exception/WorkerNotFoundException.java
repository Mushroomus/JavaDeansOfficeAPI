package com.example.deansoffice.exception;

public class WorkerNotFoundException extends RuntimeException {
    public WorkerNotFoundException() {
        super("Worker not found");
    }
}