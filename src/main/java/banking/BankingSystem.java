package banking;

import banking.dao.AccountDAO;
import banking.model.Account;
import banking.model.Card;

import java.util.Scanner;

import static java.lang.System.exit;

/**
 * The class represents a banking system
 */
public class BankingSystem {

    private Account currentAccount;
    private int amountSend;
    private boolean isLogin;
    private final CardGenerator cardGenerator;
    private final CardValidator cardValidator;
    private final AccountDAO accountDAO;
    private final Scanner scanner;

    public BankingSystem(AccountDAO accountDAO, CardValidator cardValidator, CardGenerator cardGenerator, Scanner scanner) {
        this.cardValidator = cardValidator;
        this.cardGenerator = cardGenerator;
        this.accountDAO = accountDAO;
        this.scanner = scanner;
    }

    /**
     * Display the menu, get the user's choice, process the choice, and sync the account
     */
    public void showMenu() {
        while (true) {
            showMenuInfo();
            chooseMenu();
        }
    }

    /**
     * Outputs the menu to the console depending on is user logged in or not
     */
    public void showMenuInfo() {
        if (isLogin) {
            System.out.println("""
                    1. Balance
                    2. Add income
                    3. Do transfer
                    4. Close account
                    5. Log out
                    0. Exit""");
        } else {
            System.out.println("""
                    1. Create an account
                    2. Log into account
                    0. Exit""");
        }
    }

    /**
     * Processes the passed input based on is user logged in or not
     */
    public void chooseMenu() {
        int choice = scanner.nextInt();
        if (isLogin) {
            loggedMenu(choice);
        } else {
            registerMenu(choice);
        }
    }

    /**
     * Invokes a method related of which action had chosen the not logged user
     *
     * @param choose int value with user's choice
     */
    public void registerMenu(int choose) {
        switch (choose) {
            case 1 -> createAccount();
            case 2 -> logInOption();
            case 0 -> closeMenu();
            default -> System.out.println("Wrong number");
        }
    }

    /**
     * Invokes a method related of which action had chosen the logged user
     *
     * @param choose int value with user's choice
     */
    public void loggedMenu(int choose) {
        switch (choose) {
            case 1:
                System.out.println("Balance: " + accountDAO.getBalance(currentAccount.getId()));
                break;
            case 2:
                System.out.println("Enter income:");
                int amount = scanner.nextInt();
                accountDAO.updateMoney(amount, currentAccount.getCard().getNumber());
                break;
            case 3:
                transferMoney();
                break;
            case 4:
                accountDAO.deleteAccount(currentAccount.getId());
                System.out.println("The account has been closed!");
                isLogin = false;
                currentAccount = null;
                break;
            case 5:
                break;
            case 0:
                closeMenu();
            default:
                System.out.println("Wrong number");
                break;
        }
    }

    /**
     * Makes the process of transferring money from one account to another
     */
    public void transferMoney() {
        System.out.println("Enter card number:");
        String card = scanner.next();
        if (!card.equals(accountDAO.getNumber(currentAccount.getId()))) {
            checkValid(card);
        } else {
            System.out.println("You can't transfer money to the same account!");
        }
    }

    /**
     * Checks if the entered card matches
     */
    private void checkValid(String card) {
        if (cardValidator.isValidCard(cardGenerator.getChecksumFor(card), card)) {
            enterMoneyCount(card);
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }
    }

    /**
     * Entering money for a transaction
     *
     * @param card Car number
     */
    private void enterMoneyCount(String card) {
        if (accountDAO.containCard(card)) {
            System.out.println("Enter how much money you want to transfer:");
            amountSend = scanner.nextInt();
            transaction(card);
        } else {
            System.out.println("Such a card does not exist.");
        }
    }

    /**
     * Create transaction with chosen card
     *
     * @param card Car number
     */
    private void transaction(String card) {
        if (amountSend <= accountDAO.getBalance(currentAccount.getId())) {
            accountDAO.updateMoney(amountSend, card);
            accountDAO.updateMoney(-amountSend, currentAccount.getCard().getNumber());
            System.out.println("success");
        } else {
            System.out.println("Not enough money!");
        }
    }

    /**
     * Makes the sign in process. User needs to enter its card number and PIN
     */
    public void logInOption() {
        System.out.println("Enter your card number:");
        String number = scanner.next();

        System.out.println("Enter your PIN:");
        String PIN = scanner.next();

        Account account = accountDAO.login(number, PIN);
        if (account == null) {
            System.out.println("Wrong card number or PIN");
        } else {
            System.out.println("You have successfully logged in");
            isLogin = true;
            currentAccount = account;
        }
    }

    /**
     * Creates the account and outputs its credentials to the console
     */
    private void createAccount() {
        Card card = cardGenerator.generate();
        accountDAO.insertCard(card);
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s
                %n""", card.getNumber(), card.getPIN());
    }

    public void closeMenu() {
        scanner.close();
        System.out.println("Bye!");
        exit(0);
    }
}
