import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    private static boolean loggedIn;
    private static int id;

    public static void showMenu() {
        int choose = -1;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (loggedIn) {
                loggedMenu();
            } else {
                unloggedMenu();
            }
            choose = scanner.nextInt();
            if (loggedIn) {
                switch (choose) {
                    case 1:
                        System.out.println("Balance: " + DBConnector.getBalance(id));
                        break;
                    case 2:
                        System.out.println("Enter income:");
                        int amount = scanner.nextInt();
                        DBConnector.addBalance(id, amount);
                        break;
                    case 3:
                        System.out.println("Enter card number:");
                        String card = scanner.nextLine();
                        card = scanner.nextLine();
                        if (!card.equals(DBConnector.getNumber(id))) {
                            if (Card.checkLoon(card)) {
                                if (DBConnector.containCard(card)) {
                                    System.out.println("Enter how much money you want to transfer:");
                                    int amountSend = scanner.nextInt();
                                    if (amountSend < DBConnector.getBalance(id)) {
                                        DBConnector.sendMoney(id, card, amountSend);
                                        System.out.println("success");
                                    } else {
                                        System.out.println("Not enough money!");
                                    }

                                } else {
                                    System.out.println("Such a card does not exist.");
                                }
                            } else {
                                System.out.println("Probably you made a mistake in the card number. Please try again!");
                            }
                        } else {
                            System.out.println("You can't transfer money to the same account!");
                        }

                        break;
                    case 4:
                        DBConnector.delAccount(id);
                        System.out.println("The account has been closed!");
                        loggedIn = false;
                        id = -1;
                        break;
                    case 5:
                        break;
                    case 0:
                        System.out.println("Bye!");
                        exit(0);
                    default:
                        System.out.println("Wrong number");
                        break;
                }

            } else {
                switch (choose) {
                    case 1:
                        Account account = new Account();
                        System.out.println("Your card has been created");
                        System.out.println("Your card number:");
                        System.out.println(account.getCardNumber());
                        System.out.println("Your card PIN:");
                        System.out.println(account.getPIN());
                        break;
                    case 2:
                        System.out.println("Enter your card number:");
                        String number = scanner.nextLine();
                        number = scanner.nextLine();
                        System.out.println("Enter your PIN:");
                        int PIN = scanner.nextInt();
                        int logId = Account.login(number, PIN);
                        if (logId == -1) {
                            System.out.println("Wrong card number or PIN");
                        } else {
                            System.out.println("You have successfully logged in");
                            loggedIn = true;
                            id = logId;
                        }
                        break;
                    case 0:
                        System.out.println("Bye!");
                        exit(0);
                    default:
                        System.out.println("Wrong number");
                        break;
                }
            }
        }
    }

    public static void loggedMenu() {
        System.out.println("1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit\n");
    }

    public static void unloggedMenu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit\n");
    }
}
