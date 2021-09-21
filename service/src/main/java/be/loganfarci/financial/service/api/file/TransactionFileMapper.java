package be.loganfarci.financial.service.api.file;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.service.api.account.BankAccountValidator;
import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import be.loganfarci.financial.service.api.transaction.dto.TransactionDto;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Component
public class TransactionFileMapper {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_DESCRIPTION_LENGTH = 50;
    private static final String UNKNOWN_OWNER_NAME = "Unknown";
    private static final String UNKNOWN_BANK_ACCOUNT_NAME = "Unknown";
    private static final Double DEFAULT_BANK_ACCOUNT_BALANCE = 0.0;

    private final BankAccountValidator bankAccountValidator;

    public TransactionFileMapper(BankAccountValidator bankAccountValidator) {
        this.bankAccountValidator = bankAccountValidator;
    }

    public OwnerDto readOwnerDtoFrom(BankAccount bankAccount) {
        return new OwnerDto(getOwnerNameFromBankAccount(bankAccount));
    }

    public BankAccountDto readBankAccountDtoFrom(BankAccount bankAccount) {
        return new BankAccountDto(
                getBankAccountName(bankAccount),
                readOwnerDtoFrom(bankAccount),
                getBankAccountIban(bankAccount),
                DEFAULT_BANK_ACCOUNT_BALANCE
        );
    }

    public TransactionDto readTransactionDtoFrom(Transaction transaction) {
        return new TransactionDto(
                new Date(transaction.getDate().getTime()),
                readBankAccountDtoFrom(transaction.getInternalBankAccount()),
                readBankAccountDtoFrom(transaction.getExternalBankAccount()),
                transaction.getAmount(),
                getStringOfSize(transaction.getDescription(), MAX_DESCRIPTION_LENGTH)
        );
    }

    private String getStringOfSize(String text, int size) {
        if (text.length() > size) {
            text = text.substring(0, size - 1);
        }
        return text;
    }

    private boolean hasOwner(BankAccount bankAccount) {
        return bankAccount.getOwner() != null
                && bankAccount.getOwner().getName() != null
                && !bankAccount.getOwner().getName().isBlank();
    }

    private String getOwnerNameFromBankAccount(BankAccount bankAccount) {
        String name;
        if (hasOwner(bankAccount)) {
            name = bankAccount.getOwner().getName();
        } else {
            name = UNKNOWN_OWNER_NAME;
        }
        return getStringOfSize(name, MAX_NAME_LENGTH);
    }

    private boolean hasIban(BankAccount bankAccount) {
        return bankAccount.getIban() != null
                && !bankAccount.getIban().isBlank()
                && hasValidIban(bankAccount);
    }

    private String getBankAccountName(BankAccount bankAccount) {
        String name;
        if (hasOwner(bankAccount)) {
            name = getOwnerNameFromBankAccount(bankAccount);
        } else if (hasIban(bankAccount)) {
            name = getBankAccountIban(bankAccount);
        } else {
            name = UNKNOWN_BANK_ACCOUNT_NAME;
        }
        return getStringOfSize(name, MAX_NAME_LENGTH);
    }

    private boolean hasValidIban(BankAccount bankAccount) {
        return bankAccountValidator.isValidBankAccountIban(bankAccount.getIban());
    }

    private String getBankAccountIban(BankAccount bankAccount) {
        String iban = null;
        if (hasIban(bankAccount) && hasValidIban(bankAccount)) {
            iban = bankAccount.getIban();
        }
        return iban;
    }

}
