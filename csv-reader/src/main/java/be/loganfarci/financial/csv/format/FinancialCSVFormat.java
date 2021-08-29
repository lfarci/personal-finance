package be.loganfarci.financial.csv.format;

import be.loganfarci.financial.csv.format.exception.FinancialCSVFormatException;
import be.loganfarci.financial.csv.format.parser.record.BelfiusRecordParser;
import be.loganfarci.financial.csv.format.parser.record.RecordParser;
import be.loganfarci.financial.csv.format.parser.record.SodexoRecordParser;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.csv.model.Transactions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Iterator;

public enum FinancialCSVFormat {

    BELFIUS(new BelfiusRecordParser(), StandardCharsets.ISO_8859_1, Headers.BELFIUS),
    SODEXO(new SodexoRecordParser(), StandardCharsets.UTF_8, Headers.SODEXO);

    private RecordParser<Transaction> recordParser;
    private Charset charset;
    private String[] headers;
    private char delimiter = ';';
    private boolean trim = true;

    FinancialCSVFormat(RecordParser<Transaction> recordParser, Charset charset, String[] headers) {
        this.recordParser = recordParser;
        this.headers = headers;
        this.charset = charset;
    }

    private boolean isStringEqualToUTF8EncodedValue(String csvFileValue, String expectedValue) {
        boolean equal;
        try {
            ByteArrayInputStream bytes = new ByteArrayInputStream(csvFileValue.getBytes());
            BOMInputStream in = new BOMInputStream(bytes, false);
            if (in.hasBOM()) {
                equal = Arrays.equals(in.readAllBytes(), expectedValue.getBytes(StandardCharsets.UTF_8));
            } else {
                equal = csvFileValue.equals(expectedValue);
            }
        } catch (IOException e) {
            equal = false;
        }
        return equal;
    }

    private boolean areEqual(String csvFileValue, String expectedValue) {
        if (charset.equals(StandardCharsets.UTF_8)) {
            return isStringEqualToUTF8EncodedValue(csvFileValue, expectedValue);
        }
        return csvFileValue.equals(expectedValue);
    }

    boolean isInRecord(CSVRecord record, String expectedValue) {
        return record.stream().anyMatch(value -> areEqual(value, expectedValue));
    }

    public boolean canRead(File file) throws FinancialCSVFormatException {
        try {
            Iterator<CSVRecord> records = records(new FileReader(file, charset)).iterator();
            CSVRecord record = records.next();
            return records.hasNext() && Arrays.stream(headers).allMatch(header -> isInRecord(record, header));
        } catch (IOException e) {
            throw new FinancialCSVFormatException("Could not read file: " + file.getPath());
        }
    }

    public Transactions parse(File file) throws FinancialCSVFormatException {
        try {
            return parse(new FileReader(file, charset));
        } catch (IOException e) {
            throw new FinancialCSVFormatException("Cannot reade from file: " + file.getPath());
        }
    }

    private Transactions parse(Reader reader) throws FinancialCSVFormatException {
        Transactions transactions = new Transactions();
        Iterator<CSVRecord> records = records(reader).iterator();
        if (records.hasNext())
            records.next();
        while (records.hasNext()) {
            transactions.add(recordParser.parse(records.next()));
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
