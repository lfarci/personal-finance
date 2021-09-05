package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
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


    private OwnerEntity saveOwnerEntity(OwnerDto owner) {
        OwnerEntity entity;
        if (ownerService.existsByName(owner.getName())) {
            entity = ownerService.findByName(owner.getName());
        } else {
            entity = ownerService.save(owner);
        }
        return entity;
    }

    public BankAccountEntity save(BankAccount bankAccount) {
        BankAccountEntity entity = new BankAccountEntity();
        entity.setName("Unknown bank account");
        entity.setIban(bankAccount.getIban());
//        entity.setOwner(ownerService.save(bankAccount.getOwner()));
        entity.setBalance(0.0);
        return bankAccountRepository.save(entity);
    }
}
