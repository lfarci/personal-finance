package be.loganfarci.financial.service.api.account.service;

import be.loganfarci.financial.service.api.account.model.exception.BankAccountEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.account.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.account.persistence.BankAccountRepository;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountRequestBodyDto;
import be.loganfarci.financial.service.api.account.model.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.owner.persistence.OwnerEntity;
import be.loganfarci.financial.service.api.owner.service.OwnerService;
import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return existsById(bankAccount.getId())
                || existsByIban(bankAccount.getIban())
                || existsByNameAndOwnerName(bankAccount);
    }

    public boolean existsById(Long id) {
        return id != null && bankAccountRepository.existsById(id);
    }

    public boolean existsByIban(String iban) {
        return iban != null && bankAccountRepository.existsByIban(iban);
    }

    public boolean existsByNameAndOwnerName(BankAccountDto bankAccount) {
        return hasExistingOwner(bankAccount) && exists(bankAccount.getName(), bankAccount.getOwner().getName());
    }

    public BankAccountDto findById(Long id) {
        if (existsById(id)) {
            return fromEntity(findEntityById(id));
        } else {
            throw new BankAccountEntityNotFoundException("No bank account with id: " + id);
        }
    }

    private boolean isInBankAccountSelection(BankAccountDto bankAccount, Boolean isInternal) {
        return isInternal == null || bankAccount.isInternal().equals(isInternal);
    }

    public List<BankAccountDto> findAll(Boolean isInternal) {
        System.out.println("Internal " + isInternal);
        return bankAccountRepository.findAll()
                .stream()
                .map(this::fromEntity)
                .filter(b -> isInBankAccountSelection(b, isInternal))
                .collect(Collectors.toList());
    }

    public BankAccountEntity findEntity(BankAccountDto bankAccount) {
        if (existsById(bankAccount.getId())) {
            return findEntityById(bankAccount.getId());
        }
        if (existsByIban(bankAccount.getIban())) {
            return findEntityByIban(bankAccount.getIban());
        }
        if (existsByNameAndOwnerName(bankAccount)) {
            return findEntityByNameAndOwnerName(bankAccount.getName(), bankAccount.getOwner().getName());
        }
        throw new BankAccountEntityNotFoundException("Failed to find a bank account based on IBAN or name.");
    }

    private OwnerEntity findBankAccountOwnerEntity(BankAccountDto bankAccount) {
        OwnerEntity owner;
        if (bankAccount == null) {
            throw new IllegalArgumentException(REQUIRED_BANK_ACCOUNT_ERROR);
        }
        if (bankAccount.getOwner() == null) {
            throw new IllegalArgumentException(REQUIRED_BANK_ACCOUNT_OWNER_ERROR);
        }
        if (hasExistingOwner(bankAccount)) {
            owner = ownerService.findEntityByName(bankAccount.getOwner().getName());
        } else {
            throw new OwnerNotFoundException(bankAccount.getOwner().getName());
        }
        return owner;
    }

    public BankAccountDto save(BankAccountRequestBodyDto bankAccountDto) {
        return fromEntity(save(fromBodyRequestDto(bankAccountDto)));
    }

    public BankAccountEntity save(BankAccountDto bankAccount) {
        bankAccountValidator.requireValid(bankAccount);
        OwnerEntity owner = findBankAccountOwnerEntity(bankAccount);
        BankAccountEntity entity;

        if (exists(bankAccount)) {
            throw new BankAccountEntityAlreadyExistsException("The bank account already exists.");
        } else {
            entity = new BankAccountEntity();
        }

        entity.setName(bankAccount.getName());
        entity.setOwner(owner);
        entity.setIban(bankAccount.getIban());
        entity.setBalance(bankAccount.getBalance());
        entity.setInternal(bankAccount.isInternal());

        return bankAccountRepository.save(entity);
    }

    public BankAccountDto update(Long id, BankAccountRequestBodyDto requestBodyDto) {
        BankAccountEntity entity;
        if (existsById(id)) {
            entity = findEntityById(id);
        } else {
            throw new BankAccountEntityNotFoundException("No bank account with id: " + id);
        }
        entity.setName(requestBodyDto.getName());
        entity.setOwner(ownerService.findEntityByName(requestBodyDto.getOwnerName()));
        entity.setIban(requestBodyDto.getIban());
        entity.setBalance(requestBodyDto.getBalance());
        return fromEntity(bankAccountRepository.save(entity));
    }

    public void deleteById(Long id) {
        bankAccountRepository.deleteById(id);
    }

    private BankAccountEntity findEntityById(Long id) {
        Optional<BankAccountEntity> entity = bankAccountRepository.findById(id);
        return entity.orElseThrow(() -> new BankAccountEntityNotFoundException("No bank account with id: " + id));
    }

    private BankAccountEntity findEntityByIban(String iban) {
        Optional<BankAccountEntity> entity = bankAccountRepository.findByIban(iban);
        return entity.orElseThrow(() -> new BankAccountEntityNotFoundException("No bank account with iban: " + iban));
    }

    private BankAccountEntity findEntityByNameAndOwnerName(String name, String ownerName) {
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

    private BankAccountDto fromEntity(BankAccountEntity entity) {
        OwnerDto ownerDto = ownerService.findByName(entity.getOwner().getName());
        return new BankAccountDto(
                entity.getId(),
                entity.getName(),
                ownerDto,
                entity.getIban(),
                entity.getBalance(),
                entity.isInternal()
        );
    }

    private BankAccountDto fromBodyRequestDto(BankAccountRequestBodyDto dto) {
        OwnerDto ownerDto = ownerService.findByName(dto.getOwnerName());
        return new BankAccountDto(
                dto.getId(),
                dto.getName(),
                ownerDto,
                dto.getIban(),
                dto.getBalance()
        );
    }

}
