package banking.configuration;

import org.sqlite.SQLiteDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBManager {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account
            (id INTEGER PRIMARY KEY, 
            number TEXT NOT NULL, 
            pin TEXT NOT NULL, 
            balance INTEGER DEFAULT 0)""";

    private String dbName;
    private String url;
    private final SQLiteDataSource dataSource;

    public DBManager(String dbName) {
        dataSource = new SQLiteDataSource();
        this.dbName = dbName;
        createDatabase();
        createTable();
    }

    public void createDatabase() {
        if (dbName.contains(".s3db")) {
            url = "jdbc:sqlite:" + dbName;
            dataSource.setUrl(url);
        }
    }

    public void createTable() {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(CREATE_TABLE)) {
            preparedStatement.executeUpdate();
            getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}
