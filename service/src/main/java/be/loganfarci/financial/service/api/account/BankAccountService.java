package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import be.loganfarci.financial.service.api.owner.exception.OwnerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {

    static final String REQUIRED_BANK_ACCOUNT_ERROR = "Bank account is null.";
    static final String REQUIRED_BANK_ACCOUNT_OWNER_ERROR = "Bank account owner is null.";

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
        return existsByIban(bankAccount.getIban()) || existsByNameAndOwnerName(bankAccount);
    }

    public boolean existsByIban(String iban) {
        return iban != null && bankAccountRepository.existsByIban(iban);
    }

    public boolean existsByNameAndOwnerName(BankAccountDto bankAccount) {
        return hasExistingOwner(bankAccount) && exists(bankAccount.getName(), bankAccount.getOwner().getName());
    }

    public BankAccountEntity find(BankAccountDto bankAccount) {
        if (existsByIban(bankAccount.getIban())) {
            return findByIban(bankAccount.getIban());
        }
        if (existsByNameAndOwnerName(bankAccount)) {
            return findByNameAndOwnerName(bankAccount.getName(), bankAccount.getOwner().getName());
        }
        throw new BankAccountEntityNotFoundException("Failed to find a bank account based on IBAN or name.");
    }

    public BankAccountEntity save(BankAccountDto bankAccount) {
        BankAccountEntity entity;
        OwnerEntity owner;

        if (bankAccount == null) {
            throw new IllegalArgumentException(REQUIRED_BANK_ACCOUNT_ERROR);
        }

        if (bankAccount.getOwner() == null) {
            throw new IllegalArgumentException(REQUIRED_BANK_ACCOUNT_OWNER_ERROR);
        }

        if (hasExistingOwner(bankAccount)) {
            owner = ownerService.findByName(bankAccount.getOwner().getName());
        } else {
            throw new OwnerNotFoundException(bankAccount.getOwner().getName());
        }

        bankAccountValidator.requireValid(bankAccount);

        if (exists(bankAccount)) {
            entity = find(bankAccount);
        } else {
            entity = new BankAccountEntity();
        }

        entity.setName(bankAccount.getName());
        entity.setOwner(owner);
        entity.setIban(bankAccount.getIban());
        entity.setBalance(bankAccount.getBalance());

        return bankAccountRepository.save(entity);
    }

    private BankAccountEntity findByIban(String iban) {
        Optional<BankAccountEntity> entity = bankAccountRepository.findByIban(iban);
        return entity.orElseThrow(() -> new BankAccountEntityNotFoundException("No bank account with iban: " + iban));
    }

    private BankAccountEntity findByNameAndOwnerName(String name, String ownerName) {
        Optional<BankAccountEntity> entity = bankAccountRepository.findByNameAndOwnerName(name, ownerName);
        return entity.orElseThrow(() -> new BankAccountEntityNotFoundException(name, ownerName));
    }

    private boolean hasExistingOwner(BankAccountDto bankAccount) {
        return bankAccount != null && bankAccount.getOwner() != null
                && ownerService.existsByName(bankAccount.getOwner().getName());
    }

    private boolean exists(String name, String owner) {
        return bankAccountRepository.existsByNameAndOwnerName(name, owner);
    }

}
