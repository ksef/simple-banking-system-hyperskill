package banking.database;

import banking.processing.Account;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Operations {

    private final String url;
    private final String INSERT_INTO = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private final String SELECT_CARDNUMBER = "SELECT count(number) FROM card WHERE number = (?)";
    private final String SELECT_ALL = "Select * FROM card";
    private final String SELECT_ID = "SELECT id FROM card WHERE number=? AND pin=?";
    private final String GET_CARD = "SELECT * FROM card WHERE id = ?";
    private final String UPD_BALANCE = "UPDATE card SET balance = (balance + ?) WHERE id = ?";
    private final String GET_MONEY = "UPDATE card SET balance = (balance + ?) WHERE number = ?";
    private final String SEND_MONEY = "UPDATE card SET balance = (balance - ?) WHERE id = ?";
    private final String DEL_ACC = "DELETE FROM card WHERE id = ?";

    public Operations(String url) {
        this.url = url;
    }

    public void insertCard(Account account) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO)) {
            preparedStatement.setString(1, account.getCard().getNumber());
            preparedStatement.setString(2, account.getCard().getPIN());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getCount() {
        int id = 0;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ResultSet res = stmt.executeQuery(SELECT_ALL);
            while (res.next()) {
                id = res.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public boolean containCard(String cardNumber) {
        ResultSet res;
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(SELECT_CARDNUMBER)) {
            preparedStatement.setString(1, cardNumber);
            res = preparedStatement.executeQuery();
            if (res.next()) {
                return res.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int login(String cardNumber, String PIN) {
        ResultSet res;
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(SELECT_ID)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, PIN);
            res = preparedStatement.executeQuery();
            if (res.next()) {
                return res.getInt("id");
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getBalance(int id) {
        int balance = -1;
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(GET_CARD)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            assert res != null;
            if (res.next()) {
                balance = res.getInt("balance");
                return balance;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }

    public String getNumber(int id) {
        String number = "";
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(GET_CARD)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            assert res != null;
            if (res.next()) {
                number = res.getString("number");
                return number;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void addBalance(int id, int amount) {
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(UPD_BALANCE)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMoney(int id, String card, int amountSend) {
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(GET_MONEY);
             PreparedStatement preparedStatement1 = DriverManager.getConnection(url).prepareStatement(SEND_MONEY)) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement1.setInt(1, amountSend);
            preparedStatement1.setInt(2, id);
            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delAccount(int id) {
        try (PreparedStatement preparedStatement = DriverManager.getConnection(url).prepareStatement(DEL_ACC)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
