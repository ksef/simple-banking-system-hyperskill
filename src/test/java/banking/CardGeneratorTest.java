package banking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static banking.factory.CardFactory.CARD_NUMBER;
import static banking.factory.CardFactory.createCard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

class CardGeneratorTest {

    private CardGenerator cardGenerator;

    @BeforeEach
    void setUp() {
        CardValidator cardValidator = spy(CardValidator.class);
        cardGenerator = new CardGenerator(cardValidator);
    }

    @Test
    @DisplayName("Test generating a Card object")
    void generate() {
        //given
        var expected = createCard();

        //when
        var result = cardGenerator.generate();

        //then
        assertEquals(expected.getNumber().length(), result.getNumber().length());
        assertEquals(expected.getPIN().length(), result.getPIN().length());
    }

    @Test
    @DisplayName("Test generating simple card number")
    void createCardNumber() {
        //given
        var expectedNumberLength = 16;

        //when
        var resultNumber = cardGenerator.generate().getNumber();

        //then
        assertEquals(expectedNumberLength, resultNumber.length());
    }

    @Test
    @DisplayName("Test generating a card PIN")
    void createPIN() {
        //given
        var expectedPinLength = 4;

        //when
        var resultPin = cardGenerator.generate().getPIN();

        //then
        assertEquals(expectedPinLength, resultPin.length());
    }

    @Test
    @DisplayName("Test getting a checkSum for a card number")
    void getCheckSumFor() {
        //given
        var numberWithoutCheckSum = CARD_NUMBER.substring(0, 15);
        var expected = CARD_NUMBER.charAt(15);

        //when
        var result = cardGenerator.getChecksumFor(numberWithoutCheckSum);

        //then
        assertEquals(expected, result);
    }
}
