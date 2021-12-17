package banking.model;

public class Account {

    private int id;
    private Card card;
    private int balance;

    public Account(int id, Card card, int balance) {
        this.id = id;
        this.card = card;
        this.balance = balance;
    }

    public Card getCard() {
        return card;
    }
}
