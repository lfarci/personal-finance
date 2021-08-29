package be.loganfarci.financial.csv.format.parser.record;

import be.loganfarci.financial.csv.format.exception.ColumnParserException;
import be.loganfarci.financial.csv.format.exception.RecordParserException;
import be.loganfarci.financial.csv.model.*;
import org.apache.commons.csv.CSVRecord;

import java.util.Date;

import static be.loganfarci.financial.csv.format.parser.column.BelfiusColumnParsers.*;

public class BelfiusRecordParser implements RecordParser<Transaction> {

    private static final int DATE_INDEX = 1;
    private static final int AMOUNT_INDEX = 10;
    private static final int TRANSACTION_INDEX = 8;
    private static final int COMMUNICATION_INDEX = 14;

    private static final int SENDER_IBAN_INDEX = 0;

    private static final int RECIPIENT_IBAN_INDEX = 4;
    private static final int RECIPIENT_NAME_INDEX = 5;
    private static final int RECIPIENT_BIC_INDEX = 12;
    private static final int RECIPIENT_ADDRESS_STREET_INDEX = 6;
    private static final int RECIPIENT_ADDRESS_MUNICIPALITY_INDEX = 7;

    private Municipality parseRecipientAddressMunicipality(CSVRecord record) throws RecordParserException {
        try {
            return MUNICIPALITY.parse(record.get(RECIPIENT_ADDRESS_MUNICIPALITY_INDEX));
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the recipient address municipality.", e);
        }
    }

    private Address parseRecipientAddress(CSVRecord record) throws RecordParserException {
        try {
            String streetAndNumber = STRING.parse(record.get(RECIPIENT_ADDRESS_STREET_INDEX));
            Municipality municipality = parseRecipientAddressMunicipality(record);
            return new Address(streetAndNumber, municipality);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the recipient address.", e);
        }
    }

    private Owner parseRecipientBankAccountOwner(CSVRecord record) throws RecordParserException {
        try {
            String name = STRING.parse(record.get(RECIPIENT_NAME_INDEX));
            Address address = parseRecipientAddress(record);
            return new Owner(name, address);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the recipient bank account owner.", e);
        }
    }

    private BankAccount parseRecipientBankAccount(CSVRecord record) throws RecordParserException {
        try {
            String iban = OPTIONAL_IBAN.parse(record.get(RECIPIENT_IBAN_INDEX));
            String bic = STRING.parse(record.get(RECIPIENT_BIC_INDEX));
            Owner owner = parseRecipientBankAccountOwner(record);
            return new BankAccount(owner, iban, bic);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the recipient bank account.", e);
        }
    }

    private BankAccount parseSenderBankAccount(CSVRecord record) throws RecordParserException {
        try {
            String iban = OPTIONAL_IBAN.parse(record.get(SENDER_IBAN_INDEX));
            return new BankAccount(null, iban, null);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the recipient bank account.", e);
        }
    }

    private String parseTransactionDescription(CSVRecord record) throws RecordParserException {
        try {
            String transaction = STRING.parse(record.get(TRANSACTION_INDEX));
            String communication = STRING.parse(record.get(COMMUNICATION_INDEX));
            return transaction.equals(communication)
                    ? transaction
                    : communication;
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the transaction.", e);
        }
    }

    @Override
    public Transaction parse(CSVRecord record) throws RecordParserException {
        try {
            Date date = DATE.parse(record.get(DATE_INDEX));
            Double amount = AMOUNT.parse(record.get(AMOUNT_INDEX));
            BankAccount senderBankAccount = parseSenderBankAccount(record);
            BankAccount recipientBankAccount = parseRecipientBankAccount(record);
            String description = parseTransactionDescription(record);
            return new Transaction(date, amount, senderBankAccount, recipientBankAccount, description);
        } catch (ColumnParserException e) {
            throw error(record, "could not parse the transaction.", e);
        }
    }

    private RecordParserException error(CSVRecord record, String message, Throwable cause) {
        return new RecordParserException(String.format("Failed to parse record %d: %s", record.getRecordNumber(), message), cause);
    }
}
