package com.example.deansoffice.service;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordGenerator {
    public String generateRandomPassword() {
        // Define the character set for the password
        String upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerChars = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String specialChars = "!@#$%^&*()_+-=[]{}\\|;':\",./<>?";
        
        // Concatenate the character sets
        String allChars = upperChars + lowerChars + numbers + specialChars;
        
        // Generate a random password using the character set
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(allChars.length());
            password.append(allChars.charAt(index));
        }
        
        return password.toString();
    }
}