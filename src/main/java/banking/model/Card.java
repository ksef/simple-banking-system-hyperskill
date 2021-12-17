package banking.model;

public class Card {

    private String number;
    private String PIN;

    public Card(String number, String PIN) {
        this.number = number;
        this.PIN = PIN;
    }

    public String getNumber() {
        return number;
    }

    public String getPIN() {
        return PIN;
    }
}
