package banking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static banking.factory.CardFactory.CARD_NUMBER;
import static banking.factory.CardFactory.INVALID_CARD_NUMBER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardValidatorTest {

    private static CardValidator cardValidator;
    private static CardGenerator cardGenerator;

    @BeforeAll
    static void beforeAll() {
        cardValidator = new CardValidator();
        cardGenerator = new CardGenerator(cardValidator);
    }

    @Test
    @DisplayName("Test if the card is valid")
    void isValidCard() {
        //given

        //when
        var valid = cardValidator.isValidCard(cardGenerator.getChecksumFor(CARD_NUMBER), CARD_NUMBER);

        //then
        assertTrue(valid);
    }

    @Test
    @DisplayName("Test invalid card for the validness")
    void isValidWrongCard() {
        //given

        //when
        var valid = cardValidator.isValidCard(cardGenerator.getChecksumFor(INVALID_CARD_NUMBER), INVALID_CARD_NUMBER);

        //then
        assertFalse(valid);
    }
}
