package be.loganfarci.financial.service.api.accounts.service;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.model.exception.InvalidBankAccountException;
import org.apache.commons.validator.routines.IBANValidator;
import org.springframework.stereotype.Component;

import static be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity.BANK_ACCOUNT_NAME_LENGTH;

@Component
public class BankAccountValidator {

    static class ErrorMessages {
        static final String INVALID_BANK_ACCOUNT_NAME = "Invalid bank account name: ";
        static final String INVALID_BANK_ACCOUNT_IBAN = "Invalid bank account IBAN: ";
    }

    private static final IBANValidator IBAN_VALIDATOR = IBANValidator.getInstance();

    private boolean isValidBankAccountName(String name) {
        return name != null && !name.isBlank() && name.length() <= BANK_ACCOUNT_NAME_LENGTH;
    }

    public boolean isValidBankAccountIban(String iban) {
        return iban == null || iban.isBlank() || IBAN_VALIDATOR.isValid(iban);
    }

    private boolean hasValidValues(BankAccountDto bankAccount) {
        return isValidBankAccountName(bankAccount.getName()) && isValidBankAccountIban(bankAccount.getIban());
    }

    public boolean isValid(BankAccountDto bankAccount) {
        return bankAccount != null && hasValidValues(bankAccount);
    }

    public void requireValid(BankAccountDto bankAccount) {
        if (!isValidBankAccountName(bankAccount.getName())) {
            throw error(ErrorMessages.INVALID_BANK_ACCOUNT_NAME + "\"" + bankAccount.getName() + "\"");
        }
        if (!isValidBankAccountIban(bankAccount.getIban())) {
            throw error(ErrorMessages.INVALID_BANK_ACCOUNT_IBAN + "\"" + bankAccount.getIban() + "\"");
        }
    }

    private static InvalidBankAccountException error(String message) {
        throw new InvalidBankAccountException(message);
    }

}
