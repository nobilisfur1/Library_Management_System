package services;
import models.Book;
import models.User;
import java.util.List;
import java.util.ArrayList;
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

    public Boolean addUser(User user) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO Users (user_id,name,type) VALUES (?,?,?)");

            // Setting the ? in the prepared statement to id, name, and type of user passed
            
            pstmt.setString(1, user.getUserID());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getType());

            pstmt.execute();
            
            connect.commit();

            return true;
        }
        catch (SQLException e) {           
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }
            System.out.println("Issue adding to user table: " + e);

            return false;
        }
    }

    public Boolean addBook(Book book) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement pstmt = connect.prepareStatement("INSERT INTO Books (title,author,isbn,borrower_id) VALUES (?,?,?,NULL)");

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());

            pstmt.execute();

            connect.commit();            

            return true;
        }
        catch (SQLException e) {
            
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }            
            System.out.println("Issue adding to user table: " + e);

            return false;
        }
    }

    public Boolean borrowBook(String userid, String isbn) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement check = connect.prepareStatement("SELECT borrower_id FROM Books WHERE isbn = ? AND borrower_id IS NULL");

            check.setString(1, isbn);
            ResultSet borrowId = check.executeQuery();

            connect.commit();
            
            // Will check if the book is already borrowed so I don't borrow it out twice (update the borrower id incorrectly)

            if (!borrowId.next()) {
                System.out.println(borrowId.getString(1));
                System.out.println("Book already borrowed.");
                return false;
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
            return false;
        }

        try {

            PreparedStatement stmt = connect.prepareStatement("UPDATE Books SET borrower_id = ? WHERE isbn = ?");

            stmt.setString(1, userid);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
            connect.commit();
            return true;

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }
            System.out.println("Issue setting borrower: " + e);
            return false;
        }
    }

    public Boolean returnBook(String userId, String isbn) {
        try {
            connect.setAutoCommit(false);

            PreparedStatement check = connect.prepareStatement("SELECT borrower_id FROM Books WHERE isbn = ? AND borrower_id IS NOT NULL");
            check.setString(1, isbn);
            ResultSet borrowId = check.executeQuery();

            if (!borrowId.next()) {
                System.out.println("Book not found or not borrowed.");
                return false;
            }

            if (!borrowId.getString(1).trim().equals(userId)) {
                System.out.println("Book borrowed by another user, borrowed by: " + borrowId.getString(1));
                return false;
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
            return false;
        }

        try {
            PreparedStatement stmt = connect.prepareStatement("UPDATE Books SET borrower_id = null WHERE isbn = ?");

            stmt.setString(1, isbn);
            stmt.executeUpdate();

            connect.commit();
            return true;

        }
        catch (SQLException e) {
            try {
                connect.rollback();
            }
            catch (SQLException r) {
                System.out.println("Issue rolling back: " + r);
            }

            System.out.println("Issue returning book: " + e);
            return false;
        }

    }

    public String showBooks() {
        try {
            PreparedStatement stmt = connect.prepareStatement("SELECT * FROM Books");
            ResultSet rs = stmt.executeQuery();
            String result = "";

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                String borrowerId = rs.getString("borrower_id");

                result = result.concat(title + " | " + author + " | " + isbn + " | " + borrowerId + "\n");
                }
            return result;
   
        }
        catch (SQLException e) {
            System.out.println("Issue showing books: " + e);
            return "";
        }

    }

    public String showUsers() {
        try {
            PreparedStatement stmt = connect.prepareStatement("SELECT * FROM Users");
            ResultSet rs = stmt.executeQuery();
            String result = "";

            while (rs.next()) {
                String id = rs.getString("user_id");
                String name = rs.getString("name");
                String type = rs.getString("type");

                result = result.concat(id + " | " + name + " | " + type + "\n");
            }
            return result;

        }
        catch (SQLException e) {
            System.out.println("Issue showing Users: " + e);
            return "";
        }
    }
    
    // Find book by book title.
    public String findBook(String book) {
        try {
            PreparedStatement stmt = connect.prepareStatement("SELECT * FROM Books WHERE title LIKE ? COLLATE NOCASE");
            stmt.setString(1, "%" + book.trim() + "%");

            ResultSet rs = stmt.executeQuery();
            String foundBooks = "";
            
            // Want to print a statement if no book is found.
            /*
            if (!rs.next()) {
                System.out.println("Book not found.");
            }
            */

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                String borrowerId = rs.getString("borrower_id");

                foundBooks = foundBooks.concat(title + " | " + author + " | " + isbn + " | " + borrowerId + "\n");
            }
            return foundBooks;

        }
        catch (SQLException e) {
           System.out.println("Issue finding book: " + e); 
           return "";
        }
    }

    public String findUser(String user) {
        try {
            PreparedStatement stmt = connect.prepareStatement("SELECT * FROM Users WHERE name LIKE ? COLLATE NOCASE");
            stmt.setString(1, "%" + user.trim() + "%");

            ResultSet rs = stmt.executeQuery();
            String names = "";

            while (rs.next()) {
                String id = rs.getString("user_id");
                String name = rs.getString("name");
                String type = rs.getString("type");

                names = names.concat(id + " | " + name + " | " + type + "\n");
            }
            return names;
        }
        catch (SQLException e) {
            System.out.println("Issue finding user: " + e);
            return "";
        }

    }

}
