package banking;

import banking.dao.AccountDAO;
import banking.model.Account;
import banking.model.Card;
import java.util.Scanner;
import static java.lang.System.exit;

public class BankingSystem {

    private int id;
    private int amountSend;
    private int choose;
    private boolean isLogin;
    private CardValidator cardValidator;
    private AccountDAO accountDAO;
    private Scanner scanner;
    private String card;

    public BankingSystem(AccountDAO accountDAO) {
        cardValidator = new CardValidator();
        this.accountDAO = accountDAO;
        scanner = new Scanner(System.in);
    }

    public void showMenu(){
        while (true) {
            showMenuInfo();
            chooseMenu();
        }
    }

    public void showMenuInfo() {
            if (isLogin) {
                loggedMenuInfo();
            } else {
                unloggedMenuInfo();
            }
    }

    public void chooseMenu() {
            choose = scanner.nextInt();
            if (isLogin) {
                loggedMenu(choose);
            } else {
                registerMenu(choose);
            }
    }

    private void registerMenu(int choose) {
        switch (choose) {
            case 1:
                createAccount();
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

    private void loggedMenu(int choose) {
        switch (choose) {
            case 1:
                System.out.println("Balance: " + accountDAO.getBalance(id));
                break;
            case 2:
                System.out.println("Enter income:");
                int amount = scanner.nextInt();
                accountDAO.addBalance(id, amount);
                break;
            case 3:
                transferMoney();
                break;
            case 4:
                accountDAO.delAccount(id);
                System.out.println("The account has been closed!");
                isLogin = false;
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

    private void transferMoney() {
        System.out.println("Enter card number:");
        card = scanner.next();

        if (!card.equals(accountDAO.getNumber(id))) {
            checkValid();
        } else {
            System.out.println("You can't transfer money to the same account!");
        }
    }

    private void checkValid() {
        if (cardValidator.isValidCard(card)) {
            enterMoneyCount();
        } else {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        }
    }

    private void enterMoneyCount() {
        if (accountDAO.containCard(card)) {
            System.out.println("Enter how much money you want to transfer:");
            amountSend = scanner.nextInt();
            transaction();
        } else {
            System.out.println("Such a card does not exist.");
        }
    }

    private void transaction() {
        if (amountSend < accountDAO.getBalance(id)) {
            accountDAO.sendMoney(id, amountSend);
            accountDAO.getMoney(amountSend, card);
            System.out.println("success");
        } else {
            System.out.println("Not enough money!");
        }
    }

    private void logInOption() {
        System.out.println("Enter your card number:");
        String number = scanner.next();

        System.out.println("Enter your PIN:");
        String PIN = scanner.next();

        int logId = accountDAO.login(number, PIN);
        if (logId == -1) {
            System.out.println("Wrong card number or PIN");
        } else {
            System.out.println("You have successfully logged in");
            isLogin = true;
            id = logId;
        }
    }

    private void createAccount() {
        Card card = cardValidator.generate();
        Account account = new Account(
                accountDAO.getId() + 1,
                card,
                0);
        accountDAO.insertCard(account);
        System.out.println("""
                Your card has been created
                Your card number: 
                %s
                Your card PIN:
                %s
                """.formatted(account.getCard().getNumber(), account.getCard().getPIN()));
    }

    public void loggedMenuInfo() {
        System.out.println("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit""");
    }

    public void unloggedMenuInfo() {
        System.out.println("""
                1. Create an account
                2. Log into account
                0. Exit""");
    }
}
