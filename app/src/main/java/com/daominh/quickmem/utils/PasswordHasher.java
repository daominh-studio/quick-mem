package com.daominh.quickmem.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Add password bytes to digest
            md.update(password.getBytes());

            // Get the hash's bytes
            byte[] bytes = md.digest();

            // These bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            // Get complete hashed password in hexadecimal format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("PasswordHasher", "Error hashing password", e);
        }
        return null;
    }

    // check password
    public static boolean checkPassword(String password, String hashedPassword) {
        // Hash the plain text password
        String hashOfInput = hashPassword(password);
        Log.d("PasswordHasher", "checkPassword: " + hashOfInput);

        // Compare the hashed password with the hash of the input password
        return hashOfInput != null && hashOfInput.equals(hashedPassword);
    }
}