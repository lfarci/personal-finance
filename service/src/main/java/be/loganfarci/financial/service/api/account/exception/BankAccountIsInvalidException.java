package be.loganfarci.financial.service.api.account.exception;

public class BankAccountIsInvalidException extends BankAccountEntityException {
    public BankAccountIsInvalidException(String message) {
        super(message);
    }
    public BankAccountIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
