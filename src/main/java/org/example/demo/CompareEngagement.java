package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;

public class CompareEngagement {

    private final Application1 app; // Reference to Application1 instance
    private final Scene previousScene; // The Dashboard scene to navigate back

    public CompareEngagement(Application1 app, Scene previousScene) {
        this.app = app;
        this.previousScene = previousScene;
    }

    public void displayCompareEngagement(Stage primaryStage) {
        // Create a VBox layout
        VBox compareEngagementPane = new VBox(10);
        compareEngagementPane.setPadding(new Insets(20));
        compareEngagementPane.setAlignment(Pos.CENTER);

        // Add a title label
        Label titleLabel = new Label("Compare Engagement");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create input fields for both influencers
        TextField influencer1LikesField = new TextField();
        influencer1LikesField.setPromptText("Enter Likes for Influencer 1");

        TextField influencer1CommentsField = new TextField();
        influencer1CommentsField.setPromptText("Enter Comments for Influencer 1");

        TextField influencer2LikesField = new TextField();
        influencer2LikesField.setPromptText("Enter Likes for Influencer 2");

        TextField influencer2CommentsField = new TextField();
        influencer2CommentsField.setPromptText("Enter Comments for Influencer 2");

        // Comparison result label
        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-text-fill: darkblue;");

        // Compare button
        Button compareButton = new Button("Compare");
        compareButton.setOnAction(e -> {
            try {
                // Get input from fields and validate them
                String likes1Text = influencer1LikesField.getText();
                String comments1Text = influencer1CommentsField.getText();
                String likes2Text = influencer2LikesField.getText();
                String comments2Text = influencer2CommentsField.getText();

                // Validate non-empty inputs
                if (likes1Text.isEmpty() || comments1Text.isEmpty()
                        || likes2Text.isEmpty() || comments2Text.isEmpty()) {
                    resultLabel.setText("All fields are required!");
                    return;
                }

                // Parse inputs to integers
                int likes1 = Integer.parseInt(likes1Text);
                int comments1 = Integer.parseInt(comments1Text);
                int likes2 = Integer.parseInt(likes2Text);
                int comments2 = Integer.parseInt(comments2Text);

                // Call the Application1 compareEngagement method
                String result = app.compareEngagement(likes1, comments1, likes2, comments2);

                // Display the result
                resultLabel.setText(result);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Please enter valid numeric values!");
            } catch (Exception ex) {
                resultLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Back button
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            // Navigate back to the previous scene
            primaryStage.setScene(previousScene);
        });

        // Add all components to the layout
        compareEngagementPane.getChildren().addAll(
                titleLabel,
                influencer1LikesField,
                influencer1CommentsField,
                influencer2LikesField,
                influencer2CommentsField,
                compareButton,
                resultLabel,
                backButton
        );

        // Create the scene and apply style
        Scene scene = new Scene(compareEngagementPane, 480, 640);
        String css = this.getClass().getResource("/style.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Set the scene to the stage
        primaryStage.setScene(scene);
    }
}