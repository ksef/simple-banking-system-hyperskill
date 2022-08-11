package banking.factory;

import banking.model.Account;

import static banking.factory.CardFactory.createCard;

public class AccountFactory {

    public static int ACCOUNT_ID = 1;
    public static int ACCOUNT_BALANCE = 1000;

    public static Account createAccount() {
        return new Account(ACCOUNT_ID, createCard(), ACCOUNT_BALANCE);
    }
}
