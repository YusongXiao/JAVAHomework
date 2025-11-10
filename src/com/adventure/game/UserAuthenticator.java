package com.adventure.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserAuthenticator {

    private static final String USER_DATA_DIR = "user_data/";

    public UserAuthenticator() {
        ensureDirExists(USER_DATA_DIR);
    }

    private void ensureDirExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public boolean isUsernameExists(String username) {
        return new File(USER_DATA_DIR + username + ".user").exists();
    }

    private String hashPassword(String password) {
        if (password == null || password.isEmpty()) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public boolean registerUser(String username, String password) {
        if (username == null || username.isEmpty() || !username.matches("^[a-zA-Z0-9_]+$")) {
            return false; // Invalid username
        }
        if (isUsernameExists(username)) {
            return false; // Username already exists
        }
        if (password != null && password.length() > 0 && password.length() < 3) {
            return false; // Invalid password
        }

        File userFile = new File(USER_DATA_DIR + username + ".user");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            writer.write(hashPassword(password));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyPassword(String username, String password) {
        File userFile = new File(USER_DATA_DIR + username + ".user");
        if (!userFile.exists()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String storedHash = reader.readLine();
            String inputHash = hashPassword(password);
            return (storedHash == null || storedHash.isEmpty()) ? inputHash.isEmpty() : storedHash.equals(inputHash);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
