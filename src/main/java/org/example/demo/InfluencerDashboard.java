package org.example.demo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Influencer;
import org.example.demo.model.Post;

public class InfluencerDashboard {

    private final InfluencerDAO influencerDAO;

    // Constructor to initialize DAO
    public InfluencerDashboard(InfluencerDAO influencerDAO) {
        this.influencerDAO = influencerDAO;
    }

    // Display influencer profile
    public void showInfluencerProfile(Stage primaryStage, String influencerName, Dashboard dashboard) {
        // Fetch influencer details
        Influencer influencer = influencerDAO.getInfluencerByName(influencerName);

        if (influencer == null) {
            System.out.println("Influencer not found: " + influencerName);
            return;
        }

        // Fetch influencer posts
        Post[] posts = influencerDAO.getPostsByInfluencerId(influencer.getId());

        // Profile layout
        BorderPane profileLayout = new BorderPane();

        // Profile header
        VBox profileHeader = new VBox(10);
        profileHeader.setPadding(new Insets(20));
        profileHeader.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(influencer.getName());
        nameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label platformLabel = new Label("Platform: " + influencer.getPlatform());
        Label followersLabel = new Label("Followers: " + influencer.getFollowers());

        profileHeader.getChildren().addAll(nameLabel, platformLabel, followersLabel);

        // Back button

        Button backButton = new Button("Back to Dashboard");

// Style the button (optional)
        backButton.setStyle("-fx-font-size: 14px; -fx-background-color: #6C63FF; -fx-text-fill: white; -fx-padding: 8px;");

// Set button action
        backButton.setOnAction(event -> {
            VBox dashboardPane = dashboard.createDashboardPane(primaryStage);
            Scene dashboardScene = new Scene(dashboardPane, 500, 1000);
            dashboardScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); // Add CSS stylesheet
            primaryStage.setScene(dashboardScene);
        });

// Create a VBox to position the button in the top-right corner
        VBox buttonContainer = new VBox();

        buttonContainer.setAlignment(Pos.TOP_CENTER); // Align the button to the top-right corner
        buttonContainer.getChildren().add(backButton);

// Add the button container to the root layout (top of the BorderPane)
        profileLayout.setTop(buttonContainer);


        VBox postsSection = new VBox(10);
        postsSection.setPadding(new Insets(20));
        postsSection.setAlignment(Pos.TOP_CENTER);

        Label postsTitle = new Label("Posts:");
        postsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        postsSection.getChildren().add(postsTitle);

        // Display each post
        for (Post post : posts) {
            Label postLabel = new Label(post.getDate() + ": " + post.getContent() + ", Likes: " + post.getCurrentLikes());
            postLabel.setStyle("-fx-font-size: 14px;");
            postsSection.getChildren().add(postLabel);
        }


        profileLayout.setLeft(backButton);
        profileLayout.setTop(profileHeader);
        profileLayout.setCenter(postsSection);


        // Display the profile scene
        Scene profileScene = new Scene(profileLayout, 500, 1000);
        primaryStage.setScene(profileScene);
        profileScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
    }

}