public class Main {

    public static void main(String[] args) {
//        JDBC jdbc = new JDBC();
        if (args.length > 0 && args[0].equals("-fileName")) {
            DBConnector.createDatabase(args[1]);
        }
//        JDBC.createDatabase("bank.db");
        DBConnector.createTable("card");
        Menu.showMenu();

    }
}