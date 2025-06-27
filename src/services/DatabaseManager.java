package services;
import java.sql.Connection;
import java.sql.DriverManager;
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
    
    public static DatabaseManager getInstance() {
        if (dbManager == null) {
            dbManager = new DatabaseManager();
        }
        return dbManager;
    }

}
