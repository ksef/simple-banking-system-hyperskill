package banking;

/**
 * That class represents a validator of banking cards.
 */
public class CardValidator {

    /**
     * Given a card number and sum, return true if it is valid and false otherwise <br/>
     * Card considers valid if the last digit of the number corresponds of Luhn algorithm.
     *
     * @param checksum The check sum of card.
     * @param card The card number as a string.
     * @return A boolean value.
     */
    public boolean isValidCard(char checksum, String card) {
        if (card.length() < 16) {
            return false;
        }
        return card.charAt(15) == checksum;
    }
}
