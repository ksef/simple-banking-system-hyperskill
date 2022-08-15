package banking;

import banking.model.Card;

/**
 * Class for generating a Card objects with number and a PIN
 */
public class CardGenerator {

    private final String BINumber;
    private final CardValidator cardValidator;

    public CardGenerator(CardValidator cardValidator) {
        this.BINumber = "400000";
        this.cardValidator = cardValidator;
    }

    /**
     * Create a card number and PIN
     *
     * @return A Card object.
     */
    public Card generate() {
        String number = getNewCardNumber();
        String PIN = getNewPin();
        return new Card(number, PIN);
    }

    /**
     * Creates card number by joining BINumber, account identifier and checksum
     *
     * @return a String value of card number
     */
    private String getNewCardNumber() {
        String number = BINumber + getAccIdentifier();
        do {
            char checksum = getChecksumFor(number);
            number += checksum;
        } while (!cardValidator.isValidCard(getChecksumFor(number), number));
        return number;
    }

    /**
     * Generates random numbers and converts into a String value of size that is passed as argument
     *
     * @return a String value with generated numbers
     */
    private String getAccIdentifier() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    /**
     * Creates a PIN code of the size defined in PIN_SIZE field
     *
     * @return a String value of PIN code
     */
    private String getNewPin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    /**
     * Given a string of numbers, return the check digit using a Luhn algorithm.
     *
     * @param number The credit card number you want to use.
     * @return The check sum.
     */
    public char getChecksumFor(String number) {
        int checksum = 0;
        for (int i = 0; i < 15; i++) {
            int num = number.charAt(i) - '0';
            if (i % 2 == 0) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            checksum += num;
        }
        int charNum = 10 - (checksum % 10);
        return (char) (charNum + '0');
    }
}
