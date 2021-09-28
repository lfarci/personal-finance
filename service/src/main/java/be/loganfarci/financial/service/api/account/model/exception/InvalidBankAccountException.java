package be.loganfarci.financial.service.api.account.model.exception;

public class InvalidBankAccountException extends BankAccountException {
    public InvalidBankAccountException(String message) {
        super(message);
    }
    public InvalidBankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
