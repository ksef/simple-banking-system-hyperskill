package banking.factory;

import banking.model.Card;

public class CardFactory {

    public static String CARD_NUMBER = "4000005873444682";
    public static String RECEIVER_CARD_NUMBER = "4000006565894408";
    public static String INVALID_CARD_NUMBER = "4000005544466677";
    public static String CARD_PIN = "8937";

    public static Card createCard() {
        return new Card(CARD_NUMBER, CARD_PIN);
    }
}
