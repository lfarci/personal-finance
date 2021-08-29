package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import be.loganfarci.financial.csv.format.parser.column.ColumnParsers;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ColumnParserTests {

    @Test
    public void shouldNotThrowWhenWhenIBANIsValidWithoutWhitespaces() {
        assertIBANColumnParserDoesNotThrowFor("BE85957874661106");
    }

    @Test
    public void shouldNotThrowWhenIBANIsValidWithSpaces() {
        assertIBANColumnParserDoesNotThrowFor("BE85 9578 7466 1106");
    }

    @Test
    public void shouldNotThrowWhenIBANIsValidWithTabsAndSpaces() {
        assertIBANColumnParserDoesNotThrowFor(" BE85   9578    7466   1106");
    }

    @Test
    public void shouldReturnWithoutWhiteSpaceWhenIBANHasWhitespaces() throws ColumnParserException {
        String iban = "BE85 95787466 1106";
        assertThat(ColumnParsers.IBAN.parse(iban)).isEqualTo("BE85957874661106");
    }

    @Test
    public void shouldThrowErrorWhenIBANIsNotValid() {
        String invalidIban = "BE5 95787466 110";
        ColumnParserException exception = assertThrows(
                ColumnParserException.class,
                () ->  ColumnParsers.IBAN.parse(invalidIban)
        );
        assertThat(exception.getMessage()).isEqualTo("Invalid IBAN: " + invalidIban.replaceAll("\\s+",""));
    }

    @Test
    public void shouldNotThrowErrorWhenAmountIsValidFrenchDouble() {
        assertAmountColumnParserDoesNotThrowFor("23,1");
    }

    @Test
    public void shouldReturnExpectedValueWhenAmountIsValidFrenchDouble() throws ColumnParserException {
        assertThat(ColumnParsers.AMOUNT.parse("23,1")).isEqualTo(23.1);
    }

    @Test
    public void shouldReturnExpectedValueWhenAmountIsValidFrenchNegativeDouble() throws ColumnParserException {
        assertThat(ColumnParsers.AMOUNT.parse("-23,1")).isEqualTo(-23.1);
    }

    @Test
    public void shouldThrowWhenAmountIsNotAParsableNumber() {
        assertThrowExpectedExceptionWhenAmountIs("amount");
    }

    private void assertAmountColumnParserDoesNotThrowFor(String value ){
        assertThatCode(() -> ColumnParsers.AMOUNT.parse(value)).doesNotThrowAnyException();
    }

    private void assertIBANColumnParserDoesNotThrowFor(String value ){
        assertThatCode(() -> ColumnParsers.IBAN.parse(value)).doesNotThrowAnyException();
    }

    private void assertThrowExpectedExceptionWhenAmountIs(String amount) {
        String invalidAmount = amount;
        ColumnParserException exception = assertThrows(
                ColumnParserException.class,
                () -> ColumnParsers.AMOUNT.parse(invalidAmount)
        );
        assertThat(exception.getMessage()).isEqualTo("Invalid amount: " + invalidAmount);
    }

}
