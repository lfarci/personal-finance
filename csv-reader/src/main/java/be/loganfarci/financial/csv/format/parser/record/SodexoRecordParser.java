package be.loganfarci.financial.csv.format.parser.record;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import be.loganfarci.financial.csv.format.exception.RecordParserException;
import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.csv.model.Transaction;
import org.apache.commons.csv.CSVRecord;

import java.util.Date;

import static be.loganfarci.financial.csv.format.parser.column.SodexoColumnParsers.*;

public class SodexoRecordParser implements RecordParser<Transaction> {

    private static final int DATE_INDEX = 0;
    private static final int EXTERNAL_ACCOUNT_INDEX = 1;
    private static final int AMOUNT_INDEX = 2;

    private static final String SODEXO_OWNER_NAME = "Sodexo";
    private static final Owner DEFAULT_BANK_ACCOUNT_OWNER = new Owner(SODEXO_OWNER_NAME);
    private static final BankAccount DEFAULT_BANK_ACCOUNT = new BankAccount(DEFAULT_BANK_ACCOUNT_OWNER);

    @Override
    public Transaction parse(CSVRecord record) throws RecordParserException {
        try {
            Date date = DATE.parse(record.get(DATE_INDEX));
            Double amount = AMOUNT.parse(record.get(AMOUNT_INDEX));
            Owner owner;
            if (amount < 0) {
                owner = DEBIT_RECIPIENT.parse(record.get(EXTERNAL_ACCOUNT_INDEX));
            } else {
                owner = CREDIT_RECIPIENT.parse(record.get(EXTERNAL_ACCOUNT_INDEX));
            }
            BankAccount externalBankAccount = new BankAccount(owner);
            return new Transaction(date, amount, DEFAULT_BANK_ACCOUNT, externalBankAccount);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the transaction.", e);
        }
    }

    private RecordParserException error(CSVRecord record, String message, Throwable cause) {
        return new RecordParserException(String.format("Failed to parse record %d: %s", record.getRecordNumber(), message), cause);
    }
}
