package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerMapper;
import org.springframework.stereotype.Component;

@Component
public class BankAccountMapper {

    private final static String DEFAULT_BANK_ACCOUNT_NAME = "Unknown account";
    private final static Double DEFAULT_BANK_ACCOUNT_BALANCE = 0.0;

    private final OwnerMapper ownerMapper;

    public BankAccountMapper(OwnerMapper mapper) {
        this.ownerMapper = mapper;
    }

    public BankAccountEntity toEntity(BankAccount bankAccount) {
        String iban = bankAccount.getIban();
        OwnerEntity owner = ownerMapper.toEntity(bankAccount.getOwner());
        return new BankAccountEntity(DEFAULT_BANK_ACCOUNT_NAME, owner, iban, DEFAULT_BANK_ACCOUNT_BALANCE);
    }

}
