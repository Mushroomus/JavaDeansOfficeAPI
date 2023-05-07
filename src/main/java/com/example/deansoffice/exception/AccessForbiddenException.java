package com.example.deansoffice.exception;

public class AccessForbiddenException  extends RuntimeException {
    public AccessForbiddenException() {
        super("Access forbidden");
    }
}