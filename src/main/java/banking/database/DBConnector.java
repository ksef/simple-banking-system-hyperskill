package banking.database;


import org.sqlite.SQLiteDataSource;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnector {

    private String dbName;
    private String url;
    private SQLiteDataSource dataSource = new SQLiteDataSource();
    private final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS card
            (id INTEGER PRIMARY KEY, 
            number TEXT NOT NULL, 
            pin TEXT NOT NULL, 
            balance INTEGER DEFAULT 0)""";

    public void createDatabase(String dbname) {
        dbName = dbname;
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

    public String getUrl() {
        return url;
    }
}
