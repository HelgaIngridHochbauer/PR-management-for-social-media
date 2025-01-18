package org.example.demo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.demo.model.Application1;
import org.example.demo.model.CustomException;
import org.example.demo.InfluencerDashboard;
import org.example.demo.DatabaseConnection;
import org.example.demo.model.Influencer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard {

    private final Application1 app; // Reference to the backend Application class

    public Dashboard(Application1 app) {
        this.app = app;
    }

    public VBox createDashboardPane(Stage primaryStage) {
        VBox dashboardPane = new VBox(15);
        dashboardPane.setAlignment(Pos.CENTER);
        dashboardPane.setPadding(new javafx.geometry.Insets(20));

        Label titleLabel = new Label("PR Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button influencerDashboardButton = new Button("Influencer Dashboard");
        influencerDashboardButton.setOnAction(event -> showInfluencerDashboard(primaryStage));

        Button createPostButton = new Button("Create Post");
        createPostButton.setOnAction(e -> {
            try {
                app.createPost();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button viewCalendarButton = new Button("View Calendar");
        viewCalendarButton.setOnAction(e -> {
            try {
                app.viewCalendar();
            } catch (CustomException.InvalidNumberException ex) {
                ex.printStackTrace();
                // Optionally, display an error message in the UI
            }
        });

        Button calculateFollowerGrowthButton = new Button("Calculate Follower Growth");
        calculateFollowerGrowthButton.setOnAction(e -> app.calculateFollowerGrowth());

        // Updated Compare Engagement Button
        Button compareEngagementButton = new Button("Compare Engagement");
        compareEngagementButton.setOnAction(e -> {
            // Create an instance of CompareEngagement and pass the current Dashboard Scene
            CompareEngagement compareEngagementPage = new CompareEngagement(app, primaryStage.getScene());
            compareEngagementPage.displayCompareEngagement(primaryStage); // Transition to the Compare Engagement view
        });

        Button compareFollowerGrowthButton = new Button("Compare Follower Growth");
        compareFollowerGrowthButton.setOnAction(e -> {
            app.compareFollowerGrowth();
        });

        Button addInfluencerButton = new Button("Add Influencer");
        addInfluencerButton.setOnAction(e -> {
            AddInfluencerPage addInfluencerPage = new AddInfluencerPage(app);
            VBox addInfluencerPane = addInfluencerPage.createAddInfluencerPane(primaryStage);

            // Create a new scene with the addInfluencerPane
            Scene addInfluencerScene = new Scene(addInfluencerPane, 480, 800);

            // Reapply the CSS stylesheet to the new scene
            String css = this.getClass().getResource("/style.css").toExternalForm();
            addInfluencerScene.getStylesheets().add(css);

            // Set the scene to the primary stage
            primaryStage.setScene(addInfluencerScene);
        });

        Button sortInfluencersButton = new Button("Sort Influencers");
        sortInfluencersButton.setOnAction(e -> {
            // Create an instance of SortInfluencer and pass the current Dashboard Scene
            SortInfluencer sortInfluencerPage = new SortInfluencer(app, primaryStage.getScene());
            sortInfluencerPage.displaySortInfluencer(primaryStage); // Transition to the Sort Influencer view
        });

        Button setSortCriterionButton = new Button("Set Sort Criterion");
        setSortCriterionButton.setOnAction(e -> {
            SetSortCriterion setSortCriterionPage = new SetSortCriterion(app, primaryStage.getScene());
            setSortCriterionPage.displaySetSortCriterion(primaryStage);
        });

        Button groupByPlatformButton = new Button("Group Influencers by Platform");
        groupByPlatformButton.setOnAction(e -> {
            // Create an instance of GroupByPlatform and pass the current Dashboard Scene
            GroupByPlatform groupByPlatformPage = new GroupByPlatform(app, primaryStage.getScene());
            groupByPlatformPage.displayGroupByPlatform(primaryStage); // Transition to the view
        });

        Button groupByCelebrityLevelButton = new Button("Group Influencers by Celebrity Level");
        groupByCelebrityLevelButton.setOnAction(e -> {
            // Create an instance of GroupByCelebrityLevel and pass the current Dashboard Scene
            GroupByCelebrityLevel groupByCelebrityLevelPage = new GroupByCelebrityLevel(app, primaryStage.getScene());
            groupByCelebrityLevelPage.displayGroupByCelebrityLevel(primaryStage); // Transition to the view
        });

        Button groupByCampaignButton = new Button("Group Posts by Campaign");
        groupByCampaignButton.setOnAction(e -> {
            // Create an instance of GroupByCampaign and pass the current Dashboard Scene
            GroupByCampaign groupByCampaignPage = new GroupByCampaign(app, primaryStage.getScene());
            groupByCampaignPage.displayGroupByCampaign(primaryStage); // Transition to the view
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> primaryStage.close());

        dashboardPane.getChildren().addAll(
                titleLabel,
                influencerDashboardButton,
                createPostButton,
                viewCalendarButton,
                calculateFollowerGrowthButton,
                compareEngagementButton, // Updated Button Action
                compareFollowerGrowthButton,
                addInfluencerButton,
                sortInfluencersButton,
                setSortCriterionButton,
                groupByPlatformButton,
                groupByCelebrityLevelButton,
                groupByCampaignButton,
                exitButton
        );

        return dashboardPane;
    }


    // Method to display the Influencer Dashboard
    private void showInfluencerDashboard(Stage primaryStage) {
        // Create an InfluencerDAO instance to fetch data from the database
        InfluencerDAO influencerDAO = new InfluencerDAO(DatabaseConnection.getConnection());

        // Fetch all influencers from the database
        ArrayList<Influencer> influencers = influencerDAO.getAllInfluencers();

        // If there are no influencers in the database, show a message and exit
        if (influencers.isEmpty()) {
            Label noInfluencersLabel = new Label("No influencers available in the database.");
            VBox noInfluencersPane = new VBox(noInfluencersLabel);
            noInfluencersPane.setAlignment(Pos.CENTER);

            Scene noInfluencersScene = new Scene(noInfluencersPane, 400, 200);
            primaryStage.setScene(noInfluencersScene);
            return;
        }

        // Create a dropdown menu (ComboBox) for users to select an influencer
        ComboBox<String> influencerDropdown = new ComboBox<>();
        for (Influencer influencer : influencers) {
            // Populate dropdown with influencer names
            influencerDropdown.getItems().add(influencer.getName());
        }

        // Create layout for the dropdown and a button to confirm selection
        Label selectLabel = new Label("Select an Influencer:");
        selectLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button selectButton = new Button("View Profile");

        // User action: when user selects an influencer and clicks the button
        selectButton.setOnAction(event -> {
            String selectedName = influencerDropdown.getValue();

            if (selectedName != null) {
                // Find the selected influencer by name
                Influencer selectedInfluencer = influencers.stream()
                        .filter(influencer -> influencer.getName().equals(selectedName))
                        .findFirst()
                        .orElse(null);

                if (selectedInfluencer != null) {
                    // Show the selected influencer's profile
                    InfluencerDashboard influencerProfileView = new InfluencerDashboard(influencerDAO);
                    influencerProfileView.showInfluencerProfile(primaryStage, selectedName, this);
                } else {
                    // Show an error message
                    System.out.println("Selected influencer not found.");
                }
            } else {
                // Show a message if no item is selected
                System.out.println("Please select an influencer.");
            }
        });

        // Layout for dropdown pane
        VBox dropdownPane = new VBox(15, selectLabel, influencerDropdown, selectButton);
        dropdownPane.setAlignment(Pos.CENTER);
        dropdownPane.setPadding(new javafx.geometry.Insets(20));

        // Create a scene for the dropdown menu
        Scene dropdownScene = new Scene(dropdownPane, 400, 300);

        // Set and display the scene
        primaryStage.setScene(dropdownScene);
    }


}