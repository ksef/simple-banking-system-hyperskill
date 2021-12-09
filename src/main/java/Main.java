import java.util.HashMap;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
//        JDBC jdbc = new JDBC();
        if (args.length > 0 && args[0].equals("-fileName")) {
            JDBC.createDatabase(args[1]);
        }
//        JDBC.createDatabase("bank.db");
        JDBC.createTable("card");
        Menu.showMenu();

    }
}