package services;
import models.Book;
import models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager {
    private Connection connect;
    private Statement statement;
    private static DatabaseManager dbManager;

    private DatabaseManager() {
        try {
            connect = DriverManager.getConnection("jdbc:sqlite:data/library.db");
            statement = connect.createStatement();
        }
        catch (SQLException s) {
            System.out.println("Uh oh sql exception..." + s);
        }
    }
    // Creates database manager instance
    public static DatabaseManager getInstance() {
        if (dbManager == null) {
            dbManager = new DatabaseManager();
        }
        return dbManager;
    }

    public void createTables() {
        try {
            connect.setAutoCommit(false);

            String userTable = ("CREATE TABLE IF NOT EXISTS Users (" +
                               "user_id TEXT PRIMARY KEY," +
                               "name TEXT NOT NULL," +
                               "type TEXT NOT NULL);");
            String bookTable = ("CREATE TABLE IF NOT EXISTS Books (" +
                                "title TEXT NOT NULL," +
                                "author TEXT NOT NULL," +
                                "isbn TEXT PRIMARY KEY," +
                                "borrower_id TEXT," +
                                "FOREIGN KEY(borrower_id) REFERENCES Users(user_id));");

            statement.executeUpdate(userTable);
            statement.executeUpdate(bookTable);

            connect.commit();

            System.out.println("Table creation successful.");
        }
        catch (SQLException s) {

            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue with rollback..." + r);
            }

            System.out.println("Issue creating tables..." + s);
        }
    }

    public void addUser(User user) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO Users (user_id,name,type) VALUES (?,?,?)");


            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getType());

            pstmt.execute();
            
            connect.commit();

            ResultSet rs = statement.executeQuery("SELECT * FROM sqlite_master WHERE type='table'");

        }
        catch (SQLException e) {
            
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back." + r);
            }

            System.out.println("Issue adding to user table." + e);
        }
    }

}
