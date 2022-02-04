package be.loganfarci.financial.service.api.accounts.model.exception;

public class InvalidBankAccountException extends BankAccountException {
    public InvalidBankAccountException(String message) {
        super(message);
    }
    public InvalidBankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
