package banking;

public class CardValidator {

    public boolean isValidCard(char checksum, String card) {
        if (card.length() < 16) return false;
        return card.charAt(15) == checksum;
    }
}
