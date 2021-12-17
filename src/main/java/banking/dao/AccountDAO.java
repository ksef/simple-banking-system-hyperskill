package banking.dao;

import banking.configuration.DBManager;
import banking.model.Account;
import banking.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    private final static String INSERT_INTO_ACCOUNT = "INSERT INTO account (number, pin) VALUES (?, ?)";
    private final static String SELECT_CARD_NUMBER = "SELECT count(number) FROM account WHERE number = (?)";
    private final static String SELECT_ALL = "SELECT * FROM account";
    private final static String GET_VALID_CARD = "SELECT * FROM account WHERE number=? AND pin=?";
    private final static String GET_CARD = "SELECT * FROM account WHERE id = ?";
    private final static String UPDATE_BALANCE = "UPDATE account SET balance = (balance + ?) WHERE id = ?";
    private final static String GET_MONEY = "UPDATE account SET balance = (balance + ?) WHERE number = ?";
    private final static String SEND_MONEY = "UPDATE account SET balance = (balance - ?) WHERE number = ?";
    private final static String DELETE_ACCOUNT = "DELETE FROM account WHERE id = ?";

    private DBManager dbManager;

    public AccountDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insertCard(Account account) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(INSERT_INTO_ACCOUNT   )) {
            preparedStatement.setString(1, account.getCard().getNumber());
            preparedStatement.setString(2, account.getCard().getPIN());
            preparedStatement.executeUpdate();
            dbManager.getConnection().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getId() {
        int id = 0;
        try (Statement statement = dbManager.getConnection().createStatement()) {
            ResultSet res = statement.executeQuery(SELECT_ALL);
            while (res.next()) {
                id = res.getInt("id");
            }
            dbManager.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public boolean containCard(String cardNumber) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(SELECT_CARD_NUMBER)) {
            preparedStatement.setString(1, cardNumber);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                return res.getBoolean(1);
            }
            dbManager.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Account login(String cardNumber, String PIN) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_VALID_CARD)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, PIN);
            ResultSet res = preparedStatement.executeQuery();
            dbManager.getConnection().close();
            if (res.next()) {
                return new Account(
                        res.getInt("id"),
                        new Card(res.getString("number"), res.getString("pin")),
                        res.getInt("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBalance(int id) {
        int balance = -1;
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_CARD)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            dbManager.getConnection().close();
            assert res != null;
            if (res.next()) {
                balance = res.getInt("balance");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return balance;
    }

    public String getNumber(int id) {
        String number = "";
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_CARD)) {
            preparedStatement.setInt(1, id);
            ResultSet res = preparedStatement.executeQuery();
            dbManager.getConnection().close();
            if (res.next()) {
                number = res.getString("number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public void addBalance(int id, int amount) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(UPDATE_BALANCE)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            dbManager.getConnection().close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void updateMoney(int amountSend, String card) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_MONEY)) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
            dbManager.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(int id) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(DELETE_ACCOUNT)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            dbManager.getConnection().close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
