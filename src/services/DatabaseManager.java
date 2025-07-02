package services;
import models.Book;
import models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLDataException;

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
            System.out.println("Issue connecting to db: " + s);
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
            
            // Setting queries

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
                System.out.println("Issue with rollback: " + r);
            }

            System.out.println("Issue creating tables: " + s);
        }
    }

    public void addUser(User user) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO Users (user_id,name,type) VALUES (?,?,?)");

            // Setting the ? in the prepared statement to id, name, and type of user passed
            
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getType());

            pstmt.execute();
            
            connect.commit();

        }
        catch (SQLException e) {
            
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }

            System.out.println("Issue adding to user table: " + e);
        }
    }

    public void addBook(Book book) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO Books (title,author,isbn,borrower_id) VALUES (?,?,?,NULL)");

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());

            pstmt.execute();

            connect.commit();
            

        }
        catch (SQLException e) {
            
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }
            
            System.out.println("Issue adding to user table: " + e);

        }

    }

    public void borrowBook(String userid, String isbn) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement check = connect.prepareStatement("SELECT borrower_id FROM Books WHERE isbn = ?");

            check.setString(1, isbn);
            ResultSet borrowId = check.executeQuery();

            connect.commit();
            
            // Will check if the book is already borrowed so I don't borrow it out twice (update the borrower id incorrectly)

            if (!borrowId.next()) {
                System.out.println(borrowId.getString(1));
                System.out.println("Book already borrowed.");
                return;
            }

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }
            System.out.println("Issue checking borrowed_by: " + e);
        }

        try {

            PreparedStatement stmt = connect.prepareStatement("UPDATE Books SET borrower_id = ? WHERE isbn = ?");

            stmt.setString(1, userid);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
            connect.commit();

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }

            System.out.println("Issue setting borrower: " + e);

        }
        

    }

    public void returnBook(String userId, String isbn) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement check = connect.prepareStatement("SELECT borrower_id FROM Books WHERE isbn = ?");
            check.setString(1, isbn);
            ResultSet borrowId = check.executeQuery();

            if (!borrowId.next()) {
                System.out.println("Book not found or not borrowed.");
                return;
            }

            if (!borrowId.getString(1).trim().equals(userId)) {
                System.out.println("Book borrowed by another user, borrowed by: " + borrowId.getString(1));
                return;
            }

            connect.commit();

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }

            System.out.println("Issue finding or comparing borrower id: " + e);
        }

        try {
            PreparedStatement stmt = connect.prepareStatement("UPDATE Books SET borrower_id = null WHERE isbn = ?");

            stmt.setString(1, isbn);
            stmt.executeUpdate();

            connect.commit();

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }

            System.out.println("Issue returning book: " + e);
        }

    }

    // Unfinished, working on a method to show the books table
    public void showBooks() {
        try {
            connect.setAutoCommit(false);

            PreparedStatement stmt = connect.prepareStatement("SELECT * FROM Books");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                String borrowerId = rs.getString("borrower_id");

                System.out.println(title + "\t\t" + author + "\t\t" + isbn + "\t\t" + borrowerId);
                }
   
            connect.commit();
            
        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }
            System.out.println("Issue showing books: " + e);
        }

    }

}
