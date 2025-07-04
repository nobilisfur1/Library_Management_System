package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Set title for stage
            stage.setTitle("Library Management System");
            
            // Create label
            Label label = new Label("Library Management System");
            label.setId("header-label");

            // Buttons
            Button addUser = new Button("Add User");
            Button addBook = new Button("Add Book");
            Button showUsers = new Button("Show Users");
            Button showBooks = new Button("Show Books");
            Button searchBooks = new Button("Search Books");
            Button searchUsers = new Button("Search Users");
            Button borrowBook = new Button("Borrow Book");
            Button returnBook = new Button("Return Book");
            Button exit = new Button("Exit");

            // exit button functionality
            exit.setOnAction(e -> Platform.exit());

            // Creating VBox
            VBox leftButtons = new VBox();
            leftButtons.getStyleClass().add("left-buttons");
            leftButtons.getChildren().addAll(addBook, addUser, showBooks, showUsers, searchBooks, searchUsers, borrowBook, returnBook, exit);

            // Creating border pane
            BorderPane borderPane = new BorderPane();
            borderPane.setTop(label);
            borderPane.setAlignment(label, Pos.CENTER);
            borderPane.setLeft(leftButtons);

            // Making a scene
            Scene scene = new Scene(borderPane, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

            // Set scene
            stage.setScene(scene);

            stage.show();

        }
        catch (Exception e) {
            System.out.println(e.getMessage());

        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
