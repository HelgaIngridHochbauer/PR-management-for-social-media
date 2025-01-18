package org.example.demo;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginFormTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/PR";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "23122003";

    @BeforeAll
    public static void setUpDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement insertUser = connection.prepareStatement(
                     "INSERT INTO users (name, email, password) VALUES (?, ?, ?)")) {

            // Add test user to the database
            insertUser.setString(1, "Test User");
            insertUser.setString(2, "test@example.com");
            insertUser.setString(3, "password123"); // Use raw or hashed password based on your implementation
            insertUser.executeUpdate();
        }
    }

    @AfterAll
    public static void tearDownDatabase() throws Exception {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement deleteUser = connection.prepareStatement(
                     "DELETE FROM users WHERE email = ?")) {

            // Clean up test user from the database
            deleteUser.setString(1, "test@example.com");
            deleteUser.executeUpdate();
        }
    }

    @Test
    public void testAuthenticate_ValidCredentials() throws Exception {
        LoginForm loginForm = new LoginForm();

        // Access the private authenticate method
        Method authenticateMethod = LoginForm.class.getDeclaredMethod("authenticate", String.class, String.class);
        authenticateMethod.setAccessible(true);

        // Test with valid credentials
        boolean result = (boolean) authenticateMethod.invoke(loginForm, "test@example.com", "password123");
        assertEquals(true, result, "Authentication should succeed for valid credentials");
    }

    @Test
    public void testAuthenticate_InvalidCredentials() throws Exception {
        LoginForm loginForm = new LoginForm();

        // Access the private authenticate method
        Method authenticateMethod = LoginForm.class.getDeclaredMethod("authenticate", String.class, String.class);
        authenticateMethod.setAccessible(true);

        // Test with invalid credentials
        boolean result = (boolean) authenticateMethod.invoke(loginForm, "invalid@example.com", "wrongpassword");
        assertEquals(false, result, "Authentication should fail for invalid credentials");
    }
}