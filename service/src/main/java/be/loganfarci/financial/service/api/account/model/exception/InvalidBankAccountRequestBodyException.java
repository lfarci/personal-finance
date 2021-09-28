package be.loganfarci.financial.service.api.account.model.exception;

import org.springframework.validation.BindingResult;

public class InvalidBankAccountRequestBodyException extends BankAccountException {

    private final BindingResult bindingResult;

    public InvalidBankAccountRequestBodyException(String message) {
        super(message);
        this.bindingResult = null;
    }

    public InvalidBankAccountRequestBodyException(String message, Throwable cause) {
        super(message, cause);
        this.bindingResult = null;
    }


    public InvalidBankAccountRequestBodyException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }
}
