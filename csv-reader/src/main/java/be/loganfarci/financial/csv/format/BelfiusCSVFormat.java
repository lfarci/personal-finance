package be.loganfarci.financial.csv.format;

import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.format.parser.record.BelfiusRecordParser;
import be.loganfarci.financial.csv.model.Transactions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

public class BelfiusCSVFormat implements FinancialCSVFormat {

    private static final BelfiusRecordParser PARSER = new BelfiusRecordParser();

    private char delimiter = ';';
    private boolean trim = true;

    @Override
    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void setTrim(boolean value) {
        this.trim = value;
    }

    @Override
    public Transactions parse(Reader reader) throws FinancialCSVFormatException {
        Transactions transactions = new Transactions();
        Iterator<CSVRecord> records = records(reader).iterator();
        if (records.hasNext())
            records.next();
        while (records.hasNext()) {
            transactions.add(PARSER.parse(records.next()));
        }
        return transactions;
    }

    private Iterable<CSVRecord> records(Reader reader) throws FinancialCSVFormatException {
        try {
            return getCSVFormat().parse(reader);
        } catch (IOException e) {
            throw new FinancialCSVFormatException("Failed to read the file.", e);
        }
    }

    private CSVFormat getCSVFormat() {
        return CSVFormat.DEFAULT.builder()
                .setDelimiter(delimiter)
                .setTrim(trim)
                .build();
    }
}
