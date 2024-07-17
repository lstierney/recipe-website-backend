package com.lstierneyltd.recipebackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashingUtil {

    public static void main(String[] args) {
        String passwordToHash = "YOUR_PASSWORD";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(passwordToHash);

        System.out.println("Hashed password: " + hashedPassword);
    }
}
