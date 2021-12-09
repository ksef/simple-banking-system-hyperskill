import org.sqlite.*;

import java.sql.*;

public class JDBC {
    static String dbName;
    static String url;
    static String cardTableName;
    static SQLiteDataSource dataSource = new SQLiteDataSource();


    public static void createDatabase(String dbname) {
        dbName = dbname;
        url = "jdbc:sqlite:" + dbName;
        dataSource.setUrl(url);
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTable(String name) {
        cardTableName = name;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS card ("
                    + "id INTEGER PRIMARY KEY,"
                    + "number text NOT NULL,pin TEXT NOT NULL,"
                    + "balance INTEGER DEFAULT 0)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertCard(Account account) {
        String sql = "INSERT INTO " + cardTableName + " VALUES "
                + "	(" + account.getId() + "," + account.getCardNumber() + "," + account.getPIN() + "," + account.getBalance() + ");";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            int i = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getCount() {
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ResultSet res = stmt.executeQuery("Select * from " + cardTableName + ";");
            while (res.next()) {
                id = res.getInt("id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static boolean containCard(String cardNumber) {
        String contains = "SELECT count(number) FROM " + cardTableName + " WHERE number = '" + cardNumber + "';";
        ResultSet res;
        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement stmt = con.createStatement()) {
                res = stmt.executeQuery(contains);
                if (res.next()) {
                    return res.getBoolean(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int login(String cardNumber, int PIN) {
        String login = "SELECT id FROM " + cardTableName + " WHERE number = '" + cardNumber + "' AND pin = " + PIN + ";";
        ResultSet res;
        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement stmt = con.createStatement()) {
                res = stmt.executeQuery(login);
                if (res.next()) {
                    return res.getInt("id");
                } else {
                    return -1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getBalance(int id) {
        String getBalance = "SELECT * FROM " + cardTableName + " WHERE id = " + id + ";";
        int balance = -1;
        try {
            try (Connection con = DriverManager.getConnection(url)) {
                try (Statement stmt = con.createStatement()) {
                    ResultSet res = stmt.executeQuery(getBalance);
                    assert res != null;
                    if (res.next()) {
                        balance = res.getInt("balance");
                        return balance;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }
    public static String getNumber(int id) {
        String getBalance = "SELECT * FROM " + cardTableName + " WHERE id = " + id + ";";
        String number = "";
        try {
            try (Connection con = DriverManager.getConnection(url)) {
                try (Statement stmt = con.createStatement()) {
                    ResultSet res = stmt.executeQuery(getBalance);
                    assert res != null;
                    if (res.next()) {
                        number = res.getString("number");
                        return number;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return number;
    }
    public static void addBalance(int id, int amount) {
        String addBalance = "UPDATE " + cardTableName + " SET balance = balance + " + amount + "  WHERE id = " + id + ";";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            int i = stmt.executeUpdate(addBalance);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendMoney(int id, String card, int amountSend) {
        String get = "UPDATE " + cardTableName + " SET balance = balance + " + amountSend + " WHERE number = " + card + ";";
        String send = "UPDATE " + cardTableName + " SET balance = balance - " + amountSend + " WHERE id = " + id + ";";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            int i = stmt.executeUpdate(get);
            int x = stmt.executeUpdate(send);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void delAccount(int id) {
        String del = "DELETE FROM " + cardTableName + " WHERE id = " + id + ";";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(del);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet makeQuery(String query) {
        ResultSet res;
        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement stmt = con.createStatement()) {
                res = stmt.executeQuery(query);
                if (res.next()) {
                    return res;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}