package banking;

import banking.database.DBConnector;

public class Main {
    public static void main(String[] args) {
        DBConnector connector = new DBConnector();

        if (args.length == 2 && args[0].equals("-fileName")) {
            connector.createDatabase(args[1]);
        }
        else {
            throw new IllegalArgumentException("""
                    Enter the "--fileName" and SQLite db name
                    """);
        }
        connector.createTable();
        BankingSystem bankingSystem = new BankingSystem(connector.getUrl());
        bankingSystem.showMenu();
    }
}
