package models;

public class User {

    private String name;
    private String userID;
    private String type;

    public User(String name, String userID, String type) {
    this.name = name;
    this.userID = userID;
    this.type = type;
    }

    // Getters and setters
    public String getName() { return name; }
    public String getUserID() { return userID; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return name + " (ID: " + userID + ", Type: " + type + ")";
    }
} 
