package org.example.demo;

import org.example.demo.model.Application1;
import org.example.demo.utils.InputDevice;
import org.example.demo.utils.OutputDevice;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AuthenticationApp extends Application {

    private Application1 app; // Declare Application1 instance here

    public AuthenticationApp() {
        // No-argument constructor, required by JavaFX
    }

    @Override
    public void start(Stage primaryStage) {
        // Instantiate InputDevice and OutputDevice
        InputDevice inputDevice = new InputDevice();   // Replace with actual implementation or mock
        OutputDevice outputDevice = new OutputDevice(); // Replace with actual implementation or mock

        // Initialize the Application1 instance with the required devices
        app = new Application1(inputDevice, outputDevice);
        DatabaseService databaseService = new DatabaseService();
        // Instantiate the components
        LoginForm loginForm = new LoginForm();
        RegistrationForm registrationForm = new RegistrationForm(databaseService);
        Dashboard dashboard = new Dashboard(app);

        // Create the Dashboard Pane, passing primaryStage to the method
        VBox dashboardPane = dashboard.createDashboardPane(primaryStage);

        // Define the Dashboard Scene
        Scene dashboardScene = new Scene(dashboardPane, 480, 700);

        // Instantiate CreatePost and create its scene
        CreatePost createPost = new CreatePost(app, dashboardScene); // Pass both app and dashboardScene
        VBox createPostPane = createPost.createCreatePostPane(primaryStage);
        Scene createPostScene = new Scene(createPostPane, 480, 700);

        // Add a "Create Post" button to the Dashboard's pane
        

        // Create the Login Pane using the LoginForm class
        VBox loginPane = loginForm.createLoginPane();

        // Create the Registration Pane using the RegistrationForm class
        GridPane registrationPane = registrationForm.createRegistrationPane();

        // Create the initial choice pane
        VBox choicePane = new VBox(10);
        choicePane.setAlignment(Pos.CENTER);
        Button logInButton = new Button("Log In");
        Button signUpButton = new Button("Sign Up");

        // Add buttons to the choice pane
        choicePane.getChildren().addAll(logInButton, signUpButton);

        // Define the scenes
        Scene choiceScene = new Scene(choicePane, 480, 700);
        Scene loginScene = new Scene(loginPane, 480, 700);
        Scene registerScene = new Scene(registrationPane, 480, 700);

        // Add CSS styling
        String css = this.getClass().getResource("/style.css").toExternalForm();
        choiceScene.getStylesheets().add(css);
        loginScene.getStylesheets().add(css);
        registerScene.getStylesheets().add(css);
        dashboardScene.getStylesheets().add(css);
        createPostScene.getStylesheets().add(css); // Add CSS for Create Post scene

        // Button actions to switch scenes
        logInButton.setOnAction(e -> primaryStage.setScene(loginScene));
        signUpButton.setOnAction(e -> primaryStage.setScene(registerScene));

        // Start with the choice screen
        primaryStage.setScene(choiceScene);
        primaryStage.setTitle("PR app");
        primaryStage.show();

        // Set action for successful login to navigate to Dashboard
        loginForm.setOnLoginSuccessful(() -> primaryStage.setScene(dashboardScene));
    }

    public static void main(String[] args) {
        launch(args);
    }
}

// must do: add input validation back (messages)
// implement the calculators with the database and the UI
// implement all the UI
