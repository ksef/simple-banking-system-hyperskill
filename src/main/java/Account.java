import java.util.Random;

public class Account {
    //protected static final HashMap<String, Account> accounts = new HashMap<String, Account>();
    private final Card card = new Card();
    private final int accNumber = card.getAccNumber();
    private final String cardNumber = Long.toString(card.getCardNumber());
    private int PIN;
    private int balance;
    static int count = DBConnector.getCount();
    int id;

    public Account() {
        count++;
        id = count;
        Random rand = new Random();
        this.PIN = 1000 + rand.nextInt(9000);
        //accounts.put(this.cardNumber, this);
        this.balance = 0;
        DBConnector.insertCard(this);
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getPIN() {
        return PIN;
    }

    public int getBalance() {
        return balance;
    }

    public static int login(String cardNumber, int PIN) {
        return DBConnector.login(cardNumber, PIN);
    }
}