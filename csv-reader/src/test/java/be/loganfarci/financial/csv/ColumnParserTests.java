package be.loganfarci.financial.csv;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import be.loganfarci.financial.csv.format.parser.column.ColumnParsers;
import be.loganfarci.financial.csv.model.Municipality;
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

    @Test
    public void shouldReturnExpectedMunicipalityWhenSeparatedWithSingleSpace() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("1000 Bruxelles");
        assertThat(municipality.getName()).isEqualTo("Bruxelles");
        assertThat(municipality.getZipCode()).isEqualTo(1000);
    }

    @Test
    public void shouldReturnExpectedMunicipalityWhenSeparatedWithSingleTab() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("1000  Bruxelles");
        assertThat(municipality.getName()).isEqualTo("Bruxelles");
        assertThat(municipality.getZipCode()).isEqualTo(1000);
    }

    @Test
    public void shouldReturnExpectedMunicipalityWhenSeparatedWithMultipleSpaces() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("1000        Bruxelles");
        assertThat(municipality.getName()).isEqualTo("Bruxelles");
        assertThat(municipality.getZipCode()).isEqualTo(1000);
    }

    @Test
    public void shouldReturnExpectedMunicipalityWhenSeparatedWithSurroundingSpaces() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("  1000 Bruxelles    ");
        assertThat(municipality.getName()).isEqualTo("Bruxelles");
        assertThat(municipality.getZipCode()).isEqualTo(1000);
    }

    @Test
    public void shouldReturnExpectedMunicipalityWhenSeparatedWithSurroundingTabs() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("  1000 Bruxelles  ");
        assertThat(municipality.getName()).isEqualTo("Bruxelles");
        assertThat(municipality.getZipCode()).isEqualTo(1000);
    }

    @Test
    public void shouldReturnNullWhenZipCodeIsNotANumber() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("  zipCode Bruxelles  ");
        assertThat(municipality).isNull();
    }

    @Test
    public void shouldReturnNullWhenMunicipalityIsEmpty() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("");
        assertThat(municipality).isNull();
    }

    @Test
    public void shouldReturnNullWhenMunicipalityIsBlank() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("                 ");
        assertThat(municipality).isNull();
    }

    @Test
    public void shouldReturnNullWhenMunicipalityHasMoreThanTwoTokens() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("too much tokens");
        assertThat(municipality).isNull();
    }

    @Test
    public void shouldReturnNullWhenMunicipalityHasLessThanTwoTokens() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse("one");
        assertThat(municipality).isNull();
    }

    @Test
    public void shouldReturnNullWhenMunicipalityIsNull() throws ColumnParserException {
        Municipality municipality = ColumnParsers.MUNICIPALITY.parse(null);
        assertThat(municipality).isNull();
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
