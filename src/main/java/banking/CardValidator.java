package banking;

public class CardValidator {

    public boolean isValidCard(CardGenerator generator, String card) {
        if (card.length() < 16) return false;
        char checksum = generator.getChecksumFor(card);
        if (card.charAt(15) != checksum) return false;
        return true;
    }
}
