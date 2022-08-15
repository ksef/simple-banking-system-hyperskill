package banking.dao;

import banking.configuration.DBManager;
import banking.model.Account;
import banking.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class is responsible for managing the accounts in the database
 */
public class AccountDAO {

    private static final String INSERT_INTO_ACCOUNT = "INSERT INTO account (number, pin) VALUES (?, ?)";
    private static final String SELECT_COUNT_NUMBER = "SELECT count(number) FROM account WHERE number = (?)";
    private static final String SELECT_ACCOUNT_BY_NUMBER_AND_PIN = "SELECT * FROM account WHERE number=? AND pin=?";
    private static final String GET_ACCOUNT_BY_ID = "SELECT * FROM account WHERE id = ?";
    private static final String UPDATE_BALANCE = "UPDATE account SET balance = (balance + ?) WHERE number = ?";
    private static final String DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";

    private final DBManager dbManager;

    public AccountDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insertCard(Card card) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_ACCOUNT)) {
            preparedStatement.setString(1, card.getNumber());
            preparedStatement.setString(2, card.getPIN());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Given a card number, checks for card availability
     *
     * @param cardNumber the card number of the account to get
     * @return true or false, depending on the presence of the card in the database
     */
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

    /**
     * Given a card number and PIN, return the account with that card number and PIN
     *
     * @param cardNumber the card number of the account to get
     * @param PIN        the PIN of the card
     * @return An Account object or empty one if credentials does not match.
     */
    public Account login(String cardNumber, String PIN) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_NUMBER_AND_PIN)) {
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

    /**
     * Given account ID, return the account balance
     *
     * @param id account ID on which we want to check the balance
     * @return account balance.
     */
    public int getBalance(int id) {
        int balance = -1;
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_ID)) {
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

    /**
     * Given account ID, return card number
     *
     * @param id account ID on which we want to check the balance
     * @return card number.
     */
    public String getNumber(int id) {
        String number = "";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_ID)) {
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

    /**
     * Given the amount of money and card number, update the account balance
     *
     * @param amountSend amount of money to send
     * @param card card number
     */
    public void updateMoney(int amountSend, String card) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BALANCE)) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Given an ID deletes the account
     *
     * @param id amount of money to send
     */
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
