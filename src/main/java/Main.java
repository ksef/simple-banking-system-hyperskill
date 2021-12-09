public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-fileName")) {
            DBConnector.createDatabase(args[1]);
        }
        DBConnector.createTable("card");
        Menu.showMenu();
    }
}
