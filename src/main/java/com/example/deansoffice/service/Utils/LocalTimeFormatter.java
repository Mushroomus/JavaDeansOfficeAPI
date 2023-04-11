package com.example.deansoffice.service.Utils;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class LocalTimeFormatter {

    private static final String TIME_PATTERN = "HH:mm";

    public static LocalTime parse(String timeString) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_PATTERN);
        return LocalTime.parse(timeString, formatter);
    }
}