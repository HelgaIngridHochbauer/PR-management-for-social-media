package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm {

    private Runnable onLoginSuccessful;

    // Updated with database connection credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/PR"; // Replace with your database URL
    private static final String DB_USER = "root"; // Replace with your database username
    private static final String DB_PASSWORD = "23122003"; // Replace with your database password

    // Table structure assumed for "users" table:
    // CREATE TABLE users (id INT PRIMARY KEY AUTO_INCREMENT, email VARCHAR(255), password VARCHAR(255));

    public VBox createLoginPane() {
        VBox loginPane = new VBox(15);
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(20));

        // Email label and text field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setMaxWidth(250);

        // Password label and password field
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setMaxWidth(250);

        // Login message label
        Label loginMessageLabel = new Label();

        // Image view to display welcome image
        ImageView welcomeImageView = new ImageView();
        welcomeImageView.setFitHeight(200);
        welcomeImageView.setFitWidth(200);
        welcomeImageView.setVisible(false);

        // Login button
        Button loginButton = new Button("Log In");

        // Handle login button action
        loginButton.setOnAction(event -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            if (authenticate(email, password)) {
                loginMessageLabel.setText("Login successful! Welcome back!");
                loginMessageLabel.setStyle("-fx-text-fill: green;");
                welcomeImageView.setVisible(true);
                welcomeImageView.setImage(new Image(getClass().getResource("/welcome.jpg").toExternalForm())); // Update with correct image path

                if (onLoginSuccessful != null) {
                    onLoginSuccessful.run();
                }
            } else {
                loginMessageLabel.setText("Invalid email or password.");
                loginMessageLabel.setStyle("-fx-text-fill: red;");
                welcomeImageView.setVisible(false);
            }
        });

        // Add components to login pane
        loginPane.getChildren().addAll(
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                loginButton,
                loginMessageLabel,
                welcomeImageView
        );

        return loginPane;
    }

    /**
     * Authenticate user against the database.
     */
    private boolean authenticate(String email, String password) {
        // SQL query to check the email and password
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the placeholders in the SQL query
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // If a record is found, authentication is successful
                return resultSet.next();
            }

        } catch (SQLException e) {
            e.printStackTrace(); // You can replace this with proper logging
        }

        return false; // Return false if there's any issue or no match found
    }

    public void setOnLoginSuccessful(Runnable onLoginSuccessful) {
        this.onLoginSuccessful = onLoginSuccessful;
    }
}