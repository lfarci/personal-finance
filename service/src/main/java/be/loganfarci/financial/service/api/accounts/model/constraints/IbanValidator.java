package be.loganfarci.financial.service.api.accounts.model.constraints;

import org.apache.commons.validator.routines.IBANValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<ValidIban, String> {

    @Override
    public void initialize(ValidIban constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private boolean isBlankOrEmpty(String iban) {
        return iban.isEmpty() && iban.isBlank();
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext constraintValidatorContext) {
        IBANValidator validator = IBANValidator.getInstance();
        return iban == null || (!isBlankOrEmpty(iban) && validator.isValid(iban));
    }
}
