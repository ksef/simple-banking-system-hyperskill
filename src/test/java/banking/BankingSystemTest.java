package banking;

import banking.dao.AccountDAO;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static banking.factory.AccountFactory.*;
import static banking.factory.CardFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankingSystemTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Mock
    private AccountDAO accountDAO;
    @Mock
    private CardGenerator cardGenerator;
    @Mock
    private CardValidator cardValidator;
    private Scanner scanner;
    private BankingSystem bankingSystem;

    @BeforeEach
    public void setUpStreams() {
        scanner = new Scanner("0");
        bankingSystem = new BankingSystem(accountDAO, cardValidator, cardGenerator, scanner);
        System.setOut(new PrintStream(outContent));
    }

    @SneakyThrows
    private void setField(String fieldName, Object fieldValue) {
        var field = bankingSystem.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(bankingSystem, fieldValue);
    }

    @Test
    @SneakyThrows
    @DisplayName("Test showing the menu for not logged user")
    void showMenu() {
        //given
        var input = "0";
        var baseMenu = "1. Create an account\n2. Log into account";
        scanner = new Scanner(input);

        //when
        int status = SystemLambda.catchSystemExit(() -> bankingSystem.showMenu());
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(baseMenu));
        assertEquals(0, status);
    }

    @Test
    @DisplayName("Test showing the menu for logged user")
    @SneakyThrows
    void showLoggedMenu() {
        //given
        var baseMenu = "1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out";
        setField("isLogin", true);

        //when
        int status = SystemLambda.catchSystemExit(() -> bankingSystem.showMenu());
        var result = outContent.toString().trim();

        //then
        assertEquals(0, status);
        assertTrue(result.contains(baseMenu));
    }

    @Test
    @SneakyThrows
    @DisplayName("Transferring money to another card should be successful")
    void doTransferOption() {
        //given
        var input = RECEIVER_CARD_NUMBER + "\n" + ACCOUNT_BALANCE;
        var checksum = RECEIVER_CARD_NUMBER.charAt(15);
        var expected = "success";

        when(accountDAO.getNumber(ACCOUNT_ID)).thenReturn(CARD_NUMBER);
        when(cardGenerator.getChecksumFor(RECEIVER_CARD_NUMBER)).thenReturn(checksum);
        when(cardValidator.isValidCard(checksum, RECEIVER_CARD_NUMBER)).thenReturn(true);
        when(accountDAO.containCard(RECEIVER_CARD_NUMBER)).thenReturn(true);
        when(accountDAO.getBalance(ACCOUNT_ID)).thenReturn(ACCOUNT_BALANCE);

        scanner = new Scanner(input);
        setField("scanner", scanner);
        setField("currentAccount", createAccount());

        //when
        bankingSystem.transferMoney();
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Transferring more money than account has to another card should fail")
    void doNotEnoughMoneyTransfer() {
        //given
        var input = RECEIVER_CARD_NUMBER + "\n" + ACCOUNT_BALANCE;
        var checksum = RECEIVER_CARD_NUMBER.charAt(15);
        var expected = "Not enough money!";

        when(accountDAO.getNumber(ACCOUNT_ID)).thenReturn(CARD_NUMBER);
        when(cardGenerator.getChecksumFor(RECEIVER_CARD_NUMBER)).thenReturn(checksum);
        when(cardValidator.isValidCard(checksum, RECEIVER_CARD_NUMBER)).thenReturn(true);
        when(accountDAO.containCard(RECEIVER_CARD_NUMBER)).thenReturn(true);
        when(accountDAO.getBalance(ACCOUNT_ID)).thenReturn(0);

        scanner = new Scanner(input);
        setField("scanner", scanner);
        setField("currentAccount", createAccount());

        //when
        bankingSystem.transferMoney();
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Transferring money to nonexistent card should fail")
    void doTransferToNonExistentAccount() {
        //given
        var input = RECEIVER_CARD_NUMBER + "\n" + ACCOUNT_BALANCE;
        var checksum = RECEIVER_CARD_NUMBER.charAt(15);
        var expected = "Such a card does not exist.";

        when(accountDAO.getNumber(ACCOUNT_ID)).thenReturn(CARD_NUMBER);
        when(cardGenerator.getChecksumFor(RECEIVER_CARD_NUMBER)).thenReturn(checksum);
        when(cardValidator.isValidCard(checksum, RECEIVER_CARD_NUMBER)).thenReturn(true);
        when(accountDAO.containCard(RECEIVER_CARD_NUMBER)).thenReturn(false);

        scanner = new Scanner(input);
        setField("scanner", scanner);
        setField("currentAccount", createAccount());

        //when
        bankingSystem.transferMoney();
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Transferring money to invalid card should fail")
    void doTransferToInvalidCard() {
        //given
        var input = RECEIVER_CARD_NUMBER + "\n" + ACCOUNT_BALANCE;
        var checksum = RECEIVER_CARD_NUMBER.charAt(15);
        var expected = "Probably you made a mistake in the card number. Please try again!";

        when(accountDAO.getNumber(ACCOUNT_ID)).thenReturn(CARD_NUMBER);
        when(cardGenerator.getChecksumFor(RECEIVER_CARD_NUMBER)).thenReturn(checksum);
        when(cardValidator.isValidCard(checksum, RECEIVER_CARD_NUMBER)).thenReturn(false);

        scanner = new Scanner(input);
        setField("scanner", scanner);
        setField("currentAccount", createAccount());

        //when
        bankingSystem.transferMoney();
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Transferring money to the same account should fail")
    void doTransferToSameAccount() {
        //given
        var input = CARD_NUMBER + "\n" + ACCOUNT_BALANCE;
        var expected = "You can't transfer money to the same account!";

        when(accountDAO.getNumber(ACCOUNT_ID)).thenReturn(CARD_NUMBER);

        scanner = new Scanner(input);
        setField("scanner", scanner);
        setField("currentAccount", createAccount());

        //when
        bankingSystem.transferMoney();
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Creating account should be successful")
    void createAccountTest() {
        //given
        when(cardGenerator.generate()).thenReturn(createCard());

        //when
        bankingSystem.registerMenu(1);
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(CARD_NUMBER));
    }

    @Test
    @SneakyThrows
    @DisplayName("Login to account should be successful")
    void loginToAccountTest() {
        //given
        var input = CARD_NUMBER + "\n" + CARD_PIN;
        var expected = "You have successfully logged in";
        when(accountDAO.login(CARD_NUMBER, CARD_PIN)).thenReturn(createAccount());

        scanner = new Scanner(input);
        setField("scanner", scanner);

        //when
        bankingSystem.registerMenu(2);
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }

    @Test
    @SneakyThrows
    @DisplayName("Login to account with wrong credentials should fail")
    void loginToAccountWithWrongCredentialsTest() {
        //given
        var input = CARD_NUMBER + "\n" + CARD_PIN;
        var expected = "Wrong card number or PIN";
        when(accountDAO.login(CARD_NUMBER, CARD_PIN)).thenReturn(null);

        scanner = new Scanner(input);
        setField("scanner", scanner);

        //when
        bankingSystem.registerMenu(2);
        var result = outContent.toString().trim();

        //then
        assertTrue(result.contains(expected));
    }
}
