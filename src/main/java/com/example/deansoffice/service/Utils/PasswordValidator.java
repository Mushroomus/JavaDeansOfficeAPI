package com.example.deansoffice.service.Utils;

public class PasswordValidator {
    public static boolean validatePassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*[0-9]).{6,}$";
        return password.matches(regex);
    }
}