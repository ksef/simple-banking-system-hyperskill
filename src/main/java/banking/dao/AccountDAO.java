package banking.dao;

import banking.configuration.DBManager;
import banking.model.Account;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccountDAO {

    private final static String INSERT_INTO = "INSERT INTO account (number, pin) VALUES (?, ?)";
    private final static String SELECT_CARD_NUMBER = "SELECT count(number) FROM account WHERE number = (?)";
    private final static String SELECT_ALL = "SELECT * FROM account";
    private final static String SELECT_ID = "SELECT id FROM account WHERE number=? AND pin=?";
    private final static String GET_CARD = "SELECT * FROM account WHERE id = ?";
    private final static String UPD_BALANCE = "UPDATE account SET balance = (balance + ?) WHERE id = ?";
    private final static String GET_MONEY = "UPDATE account SET balance = (balance + ?) WHERE number = ?";
    private final static String SEND_MONEY = "UPDATE account SET balance = (balance - ?) WHERE id = ?";
    private final static String DEL_ACC = "DELETE FROM account WHERE id = ?";
    private DBManager dbManager;

    public AccountDAO(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public void insertCard(Account account) {
        try (
             PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(INSERT_INTO)) {
            preparedStatement.setString(1, account.getCard().getNumber());
            preparedStatement.setString(2, account.getCard().getPIN());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int getId() {
        int id = 0;
        try (
             Statement stmt = dbManager.getConnection().createStatement()) {

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
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(SELECT_CARD_NUMBER)) {
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
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(SELECT_ID)) {
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
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_CARD)) {
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
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_CARD)) {
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
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(UPD_BALANCE)) {
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMoney(int id, int amountSend) {
        try (PreparedStatement preparedStatement1 = dbManager.getConnection().prepareStatement(SEND_MONEY)) {
            preparedStatement1.setInt(1, amountSend);
            preparedStatement1.setInt(2, id);
            preparedStatement1.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void getMoney(int amountSend, String card) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(GET_MONEY)) {
            preparedStatement.setInt(1, amountSend);
            preparedStatement.setString(2, card);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void delAccount(int id) {
        try (PreparedStatement preparedStatement = dbManager.getConnection().prepareStatement(DEL_ACC)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
