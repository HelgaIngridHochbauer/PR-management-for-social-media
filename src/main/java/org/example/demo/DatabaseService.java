package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {

    // Database connection configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PR";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "23122003";

    /**
     * Saves user credentials to the database.
     *
     * @param name     User's name
     * @param email    User's email
     * @param password User's password
     * @return true if the data was successfully saved, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean save(String name, String email, String password) throws SQLException {
        String insertQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {

            // Bind user inputs to the query
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);

            // Return true if at least one row was inserted
            return statement.executeUpdate() > 0;
        }
    }
}