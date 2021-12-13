package banking;

import banking.database.Operations;
import banking.processing.Account;
import banking.processing.Card;

import java.util.Scanner;

import static java.lang.System.exit;

public class Menu {

    private CardValidator cardValidator;
    private boolean loggedIn;
    private int id;
    private Operations operations;
    private Scanner scanner;


    public Menu(String url) {
        cardValidator = new CardValidator();
        operations = new Operations(url);
        scanner = new Scanner(System.in);
    }

    public void showMenu() {
        int choose;

        while (true) {
            if (loggedIn) {
                loggedMenu();
            } else {
                unloggedMenu();
            }

            choose = scanner.nextInt();
            if (loggedIn) {
                loggedInControl(choose);
            } else {
                loggedOutControl(choose);
            }
        }
    }

    private void loggedOutControl(int choose) {
        switch (choose) {
            case 1:
                createAccountOption();
                break;
            case 2:
                logInOption();
                break;
            case 0:
                System.out.println("Bye!");
                exit(0);
            default:
                System.out.println("Wrong number");
                break;
        }
    }

    private void loggedInControl(int choose) {
        switch (choose) {
            case 1:
                System.out.println("Balance: " + operations.getBalance(id));
                break;
            case 2:
                System.out.println("Enter income:");
                int amount = scanner.nextInt();
                operations.addBalance(id, amount);
                break;
            case 3:
                transferToOption();
                break;
            case 4:
                operations.delAccount(id);
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
    }

    private void transferToOption() {
        System.out.println("Enter card number:");
        String card = scanner.next();

        if (!card.equals(operations.getNumber(id))) {
            if (cardValidator.isValidCard(card)) {

                if (operations.containCard(card)) {
                    System.out.println("Enter how much money you want to transfer:");
                    int amountSend = scanner.nextInt();
                    if (amountSend < operations.getBalance(id)) {
                        operations.sendMoney(id, card, amountSend);
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
    }

    private void logInOption() {
        System.out.println("Enter your card number:");
        String number = scanner.next();

        System.out.println("Enter your PIN:");
        String PIN = scanner.next();

        int logId = operations.login(number, PIN);
        if (logId == -1) {
            System.out.println("Wrong card number or PIN");
        } else {
            System.out.println("You have successfully logged in");
            loggedIn = true;
            id = logId;
        }
    }

    private void createAccountOption() {
        Card card = cardValidator.generate();
        Account account = new Account(
                operations.getCount() + 1,
                card,
                0);
        operations.insertCard(account);
        System.out.println("""
                Your card has been created
                Your card number: 
                %s
                Your card PIN:
                %s
                """.formatted(account.getCard().getNumber(), account.getCard().getPIN()));
    }

    public static void loggedMenu() {
        System.out.println("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit""");
    }

    public static void unloggedMenu() {
        System.out.println("""
                1. Create an account
                2. Log into account
                0. Exit""");
    }
}
