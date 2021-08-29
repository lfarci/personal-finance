package be.loganfarci.financial.csv.format.parser.column;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import be.loganfarci.financial.csv.model.Municipality;
import be.loganfarci.financial.csv.model.Owner;
import org.apache.commons.validator.routines.IBANValidator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class SodexoColumnParsers {

    public static ColumnParser<Double> AMOUNT = (String value) -> {
        try {
            return Double.valueOf(value.replaceAll("[\\s+€]", ""));
        } catch (NumberFormatException e) {
            throw new ColumnParserException("Invalid amount: " + value);
        }
    };

    public static ColumnParser<Owner> DEBIT_RECIPIENT = (String value) -> {
        String[] tokens = value.split("\\s+");
        if (tokens.length >= 4) {
            tokens = Arrays.copyOfRange(tokens, 1, tokens.length - 2);
            return new Owner(String.join(" ", tokens));
        } else {
            throw new ColumnParserException("Invalid debit recipient: " + value);
        }
    };

    public static ColumnParser<Owner> CREDIT_RECIPIENT = (String value) -> {
        String[] tokens = value.split("\\s+");
        if (tokens.length >= 1) {
            return new Owner(tokens[tokens.length - 1]);
        } else {
            throw new ColumnParserException("Invalid credit recipient: " + value);
        }
    };

    public static ColumnParser<Date> DATE = (String value) -> {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            return format.parse(value);
        } catch (ParseException e) {
            throw new ColumnParserException("Invalid date: " + value);
        }
    };


}