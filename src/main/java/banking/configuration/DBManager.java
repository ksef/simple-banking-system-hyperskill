package banking.configuration;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {

    private final static String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account
            (id INTEGER PRIMARY KEY, 
            number TEXT NOT NULL, 
            pin TEXT NOT NULL, 
            balance INTEGER DEFAULT 0)""";
    private String dbName;
    private String url;
    private SQLiteDataSource dataSource = new SQLiteDataSource();

    public DBManager(String dbName) {
        this.dbName = dbName;
        createDatabase();
        createTable();
    }

    public void createDatabase() {
        url = "jdbc:sqlite:" + dbName;
        dataSource.setUrl(url);
    }

    public void createTable() {
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
