package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    private final static String DEFAULT_BANK_ACCOUNT_NAME = "Unknown account";
    private final static Double DEFAULT_BANK_ACCOUNT_BALANCE = 0.0;

    public BankAccountEntity toEntity(BankAccount bankAccount) {
        String iban = bankAccount.getIban();
        OwnerEntity owner = new OwnerEntity("name");
        return new BankAccountEntity(DEFAULT_BANK_ACCOUNT_NAME, owner, iban, DEFAULT_BANK_ACCOUNT_BALANCE);
    }

}
