package banking.dao;

import banking.configuration.DBManager;
import banking.model.Account;
import banking.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountDAO {

    private static final String INSERT_INTO_ACCOUNT = "INSERT INTO account (number, pin) VALUES (?, ?)";
    private static final String SELECT_COUNT_NUMBER = "SELECT count(number) FROM account WHERE number = (?)";
    private static final String SELECT_ACCOUNT_WHERE_NUMBER_AND_PIN = "SELECT * FROM account WHERE number=? AND pin=?";
    private static final String GET_ACCOUNT_WHERE_ID = "SELECT * FROM account WHERE id = ?";
    private static final String UPDATE_BALANCE = "UPDATE account SET balance = (balance + ?) WHERE id = ?";
    private static final String GET_MONEY = "UPDATE account SET balance = (balance + ?) WHERE number = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";

    private final DBManager dbManager;

    public AccountDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insertCard(Account account) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT)) {
            preparedStatement.setString(1, account.getCard().getNumber());
            preparedStatement.setString(2, account.getCard().getPIN());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean containCard(String cardNumber) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_NUMBER)) {
            preparedStatement.setString(1, cardNumber);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return res.getBoolean(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Account login(String cardNumber, String PIN) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_WHERE_NUMBER_AND_PIN)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, PIN);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return new Account(
                        res.getInt("id"),
                        new Card(res.getString("number"), res.getString("pin")),
                        res.getInt("balance")
                );
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getBalance(int id) {
        int balance = -1;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_WHERE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                balance = res.getInt("balance");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return balance;
    }

    public String getNumber(int id) {
        String number = "";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_WHERE_ID)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                number = res.getString("number");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return number;
    }

    public void addBalance(int id, int amount) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BALANCE)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateMoney(int amountSend, String card) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_MONEY)) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAccount(int id) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACCOUNT)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
