package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.account.exception.BankAccountIsInvalidException;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import be.loganfarci.financial.service.api.owner.exception.OwnerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {

    private final BankAccountValidator bankAccountValidator;
    private final BankAccountRepository bankAccountRepository;
    private final OwnerService ownerService;

    public BankAccountService(
            BankAccountValidator bankAccountValidator,
            BankAccountRepository bankAccountRepository,
            OwnerService ownerService
    ) {
        this.bankAccountValidator = bankAccountValidator;
        this.bankAccountRepository = bankAccountRepository;
        this.ownerService = ownerService;
    }

    public boolean exists(BankAccountDto bankAccount) {
        return hasExistingOwner(bankAccount) && exists(bankAccount.getName(), bankAccount.getOwner().getName());
    }

    public BankAccountEntity save(BankAccountDto bankAccount) {
        BankAccountEntity entity;
        OwnerEntity owner;

        if (bankAccount == null) {
            throw new IllegalArgumentException("Bank account is null.");
        }

        if (hasExistingOwner(bankAccount)) {
            owner = ownerService.findByName(bankAccount.getOwner().getName());
        } else {
            throw new OwnerNotFoundException(bankAccount.getOwner().getName());
        }

        if (exists(bankAccount)) {
            entity = find(bankAccount);
        } else {
            entity = new BankAccountEntity();
        }

        bankAccountValidator.requireValid(bankAccount);
        entity.setName(bankAccount.getName());
        entity.setOwner(owner);
        entity.setIban(bankAccount.getIban());
        entity.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(entity);
    }

    private BankAccountEntity findByNameAndOwnerName(String name, String ownerName) {
        Optional<BankAccountEntity> entity = bankAccountRepository.findByNameAndOwnerName(name, ownerName);
        return entity.orElseThrow(() -> new BankAccountEntityNotFoundException(name, ownerName));
    }

    private BankAccountEntity find(BankAccountDto bankAccount) {
        return findByNameAndOwnerName(bankAccount.getName(), bankAccount.getOwner().getName());
    }

    private boolean isValidBankAccount(BankAccountDto bankAccount) {
        return bankAccountValidator.isValid(bankAccount);
    }

    private boolean hasExistingOwner(BankAccountDto bankAccount) {
        return bankAccount != null && bankAccount.getOwner() != null
                && ownerService.existsByName(bankAccount.getOwner().getName());
    }

    private boolean exists(String name, String owner) {
        return bankAccountRepository.existsByNameAndOwnerName(name, owner);
    }

}
