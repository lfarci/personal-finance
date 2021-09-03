package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.service.api.owner.OwnerService;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {

    private final BankAccountMapper bankAccountMapper;
    private final BankAccountRepository bankAccountRepository;
    private final OwnerService ownerService;

    public BankAccountService(
            BankAccountMapper bankAccountMapper,
            BankAccountRepository bankAccountRepository,
            OwnerService ownerService
    ) {
        this.bankAccountMapper = bankAccountMapper;
        this.bankAccountRepository = bankAccountRepository;
        this.ownerService = ownerService;
    }

    public BankAccountEntity save(BankAccount bankAccount) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setName("Unknown bank account");
        entity.setIban(bankAccount.getIban());
        entity.setOwner(ownerService.save(bankAccount.getOwner()));
        entity.setBalance(0.0);
        return bankAccountRepository.save(entity);
    }
}
