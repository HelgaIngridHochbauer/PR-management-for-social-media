package org.example.demo;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

import java.util.concurrent.CompletableFuture;

public class AddInfluencerPage {
    private final Application1 app;

    public AddInfluencerPage(Application1 app) {
        this.app = app;
    }

    public VBox createAddInfluencerPane(Stage primaryStage) {
        VBox addInfluencerPane = new VBox(10);
        addInfluencerPane.setPadding(new Insets(20));
        addInfluencerPane.setAlignment(Pos.CENTER);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label platformLabel = new Label("Platform:");
        TextField platformField = new TextField();

        Label followersLabel = new Label("Number of Followers:");
        TextField followersField = new TextField();

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String platform = platformField.getText();
            String followersText = followersField.getText();

            // Input validation
            if (name.isEmpty() || platform.isEmpty() || followersText.isEmpty()) {
                showValidationMessage("All fields are required!");
                return;
            }

            try {
                int followers = Integer.parseInt(followersText);

                // Asynchronous task for adding the influencer
                CompletableFuture.runAsync(() -> {
                    try {
                        // Call the method in the application to add an influencer. Task offloaded to background thread.
                        app.addInfluencer(name, platform, followers);

                        // Use Platform.runLater to update the UI on the JavaFX Application Thread
                        Platform.runLater(() -> {
                            // Navigate back to the dashboard or display a confirmation message
                            Scene dashboardScene = new Scene(new Dashboard(app).createDashboardPane(primaryStage), 480, 800);
                            String css = this.getClass().getResource("/style.css").toExternalForm();
                            dashboardScene.getStylesheets().add(css);

                            primaryStage.setScene(dashboardScene);
                        });
                    } catch (Exception ex) {
                        // Handle any background task errors
                        ex.printStackTrace();
                        Platform.runLater(() -> showValidationMessage("Failed to save influencer: " + ex.getMessage()));
                    }
                });
            } catch (NumberFormatException ex) {
                // Show an error message if the followers input is invalid
                showValidationMessage("Followers must be a valid number!");
            }
        });

        addInfluencerPane.getChildren().addAll(nameLabel, nameField, platformLabel, platformField, followersLabel, followersField, saveButton);

        // Create the add influencer scene
        Scene addInfluencerScene = new Scene(addInfluencerPane, 480, 800);

        // Apply CSS
        String css = this.getClass().getResource("/style.css").toExternalForm();
        addInfluencerScene.getStylesheets().add(css);

        primaryStage.setScene(addInfluencerScene);

        return addInfluencerPane;
    }

    /**
     * Displays an error or validation message to the user.
     *
     * @param message The message to display.
     */
    private void showValidationMessage(String message) {
        Stage messageStage = new Stage();
        VBox messagePane = new VBox(10);
        messagePane.setAlignment(Pos.CENTER);
        messagePane.setPadding(new Insets(20));

        Label messageLabel = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> messageStage.close());

        messagePane.getChildren().addAll(messageLabel, closeButton);

        Scene scene = new Scene(messagePane, 300, 150);
        messageStage.setScene(scene);
        messageStage.show();
    }
}