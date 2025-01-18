package org.example.demo;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class RegistrationForm {

    private DatabaseService databaseService;

    // Default constructor (required for JavaFX or legacy code)
    public RegistrationForm() {
    }

    // Constructor that allows injecting DatabaseService
    public RegistrationForm(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    // Setter for DatabaseService (can be called after instantiation if default constructor is used)
    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    // Method to create the Registration GridPane
    public GridPane createRegistrationPane() {
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        nameField.setPromptText("Enter your name");
        emailField.setPromptText("Enter your email");
        passwordField.setPromptText("Enter your password");
        messageLabel.setStyle("-fx-text-fill: red;");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.getStyleClass().add("root");

        gridPane.add(new Label("Name:"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("Email:"), 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(new Label("Password:"), 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(registerButton, 1, 3);
        gridPane.add(messageLabel, 1, 4);

        // Handle Register Button action
        registerButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (validateInput(name, email, password)) {
                saveCredentialsToDatabase(name, email, password, messageLabel);
            } else {
                messageLabel.setText("Invalid input. Please check your details.");
            }
        });

        return gridPane;
    }

    private void saveCredentialsToDatabase(String name, String email, String password, Label messageLabel) {
        try {
            if (databaseService == null) {
                throw new IllegalStateException("DatabaseService is not set!");
            }

            boolean success = databaseService.save(name, email, password);
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Registration successful!");
            } else {
                messageLabel.setText("Failed to register. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error saving credentials. Please contact support.");
        }
    }

    private boolean validateInput(String name, String email, String password) {
        return !name.isBlank() && isValidEmail(email) && !password.isBlank();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@");
    }
}