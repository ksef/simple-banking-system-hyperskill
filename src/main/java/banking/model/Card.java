package banking.model;

public class Card {

    private String number;
    private String PIN;

    public Card(String number, StringBuilder PIN) {
        this.number = number;
        this.PIN = String.valueOf(PIN);
    }

    public String getNumber() {
        return number;
    }

    public String getPIN() {
        return PIN;
    }
}
