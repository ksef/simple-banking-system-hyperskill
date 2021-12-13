package banking;

import banking.database.DBConnector;

public class Main {
    public static void main(String[] args) {
        DBConnector connector = new DBConnector();

        if (args.length > 0 && args[0].equals("-fileName")) {
            connector.createDatabase(args[1]);
        }
        connector.createTable();
        Menu menu = new Menu(connector.getUrl());
        menu.showMenu();
    }
}
