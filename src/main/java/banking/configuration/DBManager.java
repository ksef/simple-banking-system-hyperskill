package banking.configuration;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The DBManager class is responsible for creating the database and the table
 */
public class DBManager {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account 
            (id INTEGER PRIMARY KEY, 
            number TEXT NOT NULL, 
            pin TEXT NOT NULL, 
            balance INTEGER DEFAULT 0)""";

    private final String dbName;
    private String url;
    private final SQLiteDataSource dataSource;

    public DBManager(String dbName) {
        dataSource = new SQLiteDataSource();
        this.dbName = dbName;
        createDatabase();
        createTable();
    }

    /**
     * Create the database
     */
    private void createDatabase() {
        if (dbName.contains(".s3db")) {
            url = "jdbc:sqlite:" + dbName;
            dataSource.setUrl(url);
        } else {
            throw new IllegalArgumentException("Wrong file extension: " + dbName);
        }
    }

    /**
     * Create a table called 'account' in the database
     */
    private void createTable() {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get connection to the database
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
