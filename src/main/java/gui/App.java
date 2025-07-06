package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import models.Book;
import models.User;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import services.DatabaseManager;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        try {
            DatabaseManager dbm = DatabaseManager.getInstance();
            
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

            VBox center = new VBox();

            // Creating border pane
            BorderPane borderPane = new BorderPane();
            borderPane.setTop(label);
            borderPane.setAlignment(label, Pos.CENTER);
            borderPane.setLeft(leftButtons);
            borderPane.setCenter(center);

            // button actions

            // Add book button
            addBook.setOnAction(e -> {
                center.getChildren().clear();

                center.getChildren().add(new Label("Title:"));
                TextArea titleField = new TextArea();
                titleField.setPrefRowCount(1);
                center.getChildren().add(titleField);

                center.getChildren().add(new Label("Author:"));
                TextArea authorField = new TextArea();
                authorField.setPrefRowCount(1);
                center.getChildren().add(authorField);

                center.getChildren().add(new Label("ISBN:"));
                TextArea isbnField = new TextArea();
                isbnField.setPrefRowCount(1);
                center.getChildren().add(isbnField);

                Button addBookButton = new Button("Add");
                center.getChildren().add(addBookButton);
                addBookButton.setOnAction(r -> {
                    Book newBook = new Book(titleField.getText(), authorField.getText(), isbnField.getText());
                    if (dbm.addBook(newBook)) {
                        center.getChildren().add(new TextArea("Book added!"));
                    }
                    else {
                        center.getChildren().add(new TextArea("Failed to add book..."));
                    }
                });

            });
            

            // Add users button
            addUser.setOnAction(e -> {
                center.getChildren().clear();

                center.getChildren().add(new Label("Name:"));
                TextArea nameField = new TextArea();
                nameField.setPrefRowCount(1);
                center.getChildren().add(nameField);

                center.getChildren().add(new Label("User ID:"));
                TextArea idField = new TextArea();
                idField.setPrefRowCount(1);
                center.getChildren().add(idField);

                center.getChildren().add(new Label("Type:"));
                TextArea typeField = new TextArea();
                typeField.setPrefRowCount(1);
                center.getChildren().add(typeField);

                Button addUserButton = new Button("Add");
                center.getChildren().add(addUserButton);
                addUserButton.setOnAction(r -> {
                    User newUser = new User(nameField.getText(), idField.getText(), typeField.getText());
                    if (dbm.addUser(newUser)) {
                        TextArea success = new TextArea("User added!");
                        center.getChildren().add(success);
                    }
                    else {
                        TextArea failure = new TextArea("Failed to add user...");
                        center.getChildren().add(failure);
                    }
                });

            });

            // Show users button
            showUsers.setOnAction(e -> {
                TextArea textArea = new TextArea(dbm.showUsers());
                center.getChildren().clear();
                center.getChildren().add(textArea);
            });

            // Show books button
            showBooks.setOnAction(e -> {
                TextArea textArea = new TextArea(dbm.showBooks());
                center.getChildren().clear();
                center.getChildren().add(textArea);
            });

            // Search books button
            searchBooks.setOnAction(e -> {
                center.getChildren().clear();
                center.getChildren().add(new Label("Title:"));
                TextArea titleField = new TextArea();
                titleField.setPrefRowCount(1);
                Button searchTitle = new Button("Search");
                center.getChildren().addAll(titleField, searchTitle);
                searchTitle.setOnAction(r -> {
                    String bookTitle = titleField.getText();
                    TextArea textArea = new TextArea(dbm.findBook(bookTitle));
                    center.getChildren().clear();
                    center.getChildren().add(textArea);
                });
            });

            // Search users button
            searchUsers.setOnAction(e -> {
                center.getChildren().clear();
                center.getChildren().add(new Label("Name:"));
                TextArea nameField = new TextArea();
                nameField.setPrefRowCount(1);
                Button searchName = new Button("Search");
                center.getChildren().addAll(nameField, searchName);
                searchName.setOnAction(r -> {
                    String userName = nameField.getText();
                    TextArea textArea = new TextArea(dbm.findUser(userName));
                    center.getChildren().clear();
                    center.getChildren().add(textArea);
                });
            });

            // Borrow book button
            borrowBook.setOnAction(e -> {
                center.getChildren().clear();

                center.getChildren().add(new Label("User id:"));
                TextArea idField = new TextArea();
                idField.setPrefRowCount(1);
                center.getChildren().add(idField);

                center.getChildren().add(new Label("ISBN:"));
                TextArea isbnField = new TextArea();
                isbnField.setPrefRowCount(1);
                center.getChildren().add(isbnField);

                Button borrowButton = new Button("Borrow");
                center.getChildren().add(borrowButton);
                borrowButton.setOnAction(r -> {
                    String userId = idField.getText();
                    String isbn = isbnField.getText();

                    if (dbm.borrowBook(userId, isbn)) {
                        TextArea success = new TextArea("Successfully borrowed!");
                        success.setPrefRowCount(1);
                        center.getChildren().clear();
                        center.getChildren().add(success);
                    }
                    else {
                        TextArea failure = new TextArea("Book borrow failed...");
                        failure.setPrefRowCount(1);
                        center.getChildren().clear();
                        center.getChildren().add(failure);
                    }
                });

            });

            // Return book button
            returnBook.setOnAction(e -> {
                center.getChildren().clear();

 
                center.getChildren().add(new Label("User id:"));
                TextArea idField = new TextArea();
                idField.setPrefRowCount(1);
                center.getChildren().add(idField);

                center.getChildren().add(new Label("ISBN:"));
                TextArea isbnField = new TextArea();
                isbnField.setPrefRowCount(1);
                center.getChildren().add(isbnField);

                Button returnButton = new Button("Return");
                center.getChildren().add(returnButton);               
                returnButton.setOnAction(r -> {
                    String userId = idField.getText();
                    String isbn = isbnField.getText();
                    
                    if (dbm.returnBook(userId, isbn)) {
                        TextArea success = new TextArea("Book returned");
                        success.setPrefRowCount(1);
                        center.getChildren().clear();
                        center.getChildren().add(success);
                    }
                    else {
                        TextArea failure = new TextArea("Book return failed...");
                        failure.setPrefRowCount(1);
                        center.getChildren().clear();
                        center.getChildren().add(failure);
                    }
                });

            });
            
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

    public String showBooks() {
        DatabaseManager dbm = DatabaseManager.getInstance();

        return dbm.showBooks();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
