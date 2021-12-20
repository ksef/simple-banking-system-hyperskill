package banking;

import banking.configuration.DBManager;
import banking.dao.AccountDAO;

public class Main {
    public static void main(String[] args) {
        if (args.length == 2 && args[0].equals("-fileName")) {
            DBManager dbManager = new DBManager(args[1]);
            AccountDAO accountDAO = new AccountDAO(dbManager);
            BankingSystem bankingSystem = new BankingSystem(accountDAO);
            bankingSystem.showMenu();
        } else {
            throw new IllegalArgumentException("Enter the '-fileName' and SQLite db name");
        }
    }
}
