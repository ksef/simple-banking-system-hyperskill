package banking;

import banking.model.Card;

public class CardGenerator {

    private final String BINumber;
    private final CardValidator cardValidator;

    public CardGenerator(CardValidator cardValidator) {
        this.BINumber = "400000";
        this.cardValidator = cardValidator;
    }

    public Card generate() {
        String number = getNewCardNumber();
        String PIN = getNewPin();
        return new Card(number, PIN);
    }

    private String getNewCardNumber() {
        String number = BINumber + getAccIdentifier();
        do {
            char checksum = getChecksumFor(number);
            number += checksum;
        } while (!cardValidator.isValidCard(getChecksumFor(number), number));
        return number;
    }

    private String getAccIdentifier() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    private String getNewPin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

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

    public CardValidator getCardValidator() {
        return cardValidator;
    }
}
