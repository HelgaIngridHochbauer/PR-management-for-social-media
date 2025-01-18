package org.example.demo;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectionTest {

    @Test
    void testGetConnection() {
        // Attempt to get a connection
        Connection connection = DatabaseConnection.getConnection();

        // Assert that the connection is not null
        assertNotNull(connection, "Database connection should not be null");

        // Close the connection if it's open
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed successfully!");
            }
        } catch (Exception e) {
            fail("An error occurred while closing the connection: " + e.getMessage());
        }
    }
}

