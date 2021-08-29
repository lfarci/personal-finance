package be.loganfarci.financial.csv.format.parser.column;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import org.apache.commons.validator.routines.IBANValidator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ColumnParsers {

    public static ColumnParser<Double> AMOUNT = (String value) -> {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.FRENCH);
            Number number = format.parse(value);
            return number.doubleValue();
        } catch (ParseException e) {
            throw new ColumnParserException("Invalid amount: " + value);
        }
    };

    public static ColumnParser<String> IBAN = (String value) -> {
        IBANValidator validator = IBANValidator.getInstance();
        value = value.replaceAll("\\s+","");
        if (!validator.isValid(value)) {
            throw new ColumnParserException("Invalid IBAN: " + value);
        }
        return value;
    };

    public static ColumnParser<String> OPTIONAL_IBAN = (String value) -> {
        IBANValidator validator = IBANValidator.getInstance();
        if (value == null && value.isBlank() && !validator.isValid(value)) {
            return "";
        } else {
            return value;
        }
    };

    public static ColumnParser<Date> DATE = (String value) -> {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            return format.parse(value);
        } catch (ParseException e) {
            throw new ColumnParserException("Invalid date: " + value);
        }
    };

    public static ColumnParser<String> STRING = (String value) -> value;

}
