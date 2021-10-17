package be.loganfarci.financial.service.api.transaction.service;

import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.model.exception.BankAccountEntityNotFoundException;
import be.loganfarci.financial.service.api.category.model.dto.TransactionCategoryDto;
import be.loganfarci.financial.service.api.category.persistence.TransactionCategoryEntity;
import be.loganfarci.financial.service.api.category.service.TransactionCategoryService;
import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.service.OwnerService;
import be.loganfarci.financial.service.api.transaction.model.dto.Transaction;
import be.loganfarci.financial.service.api.transaction.model.dto.WriteTransactionDto;
import be.loganfarci.financial.service.api.transaction.model.exception.TransactionBadRequestException;
import be.loganfarci.financial.service.api.transaction.model.exception.TransactionEntityException;
import be.loganfarci.financial.service.api.transaction.model.exception.TransactionNotFoundException;
import be.loganfarci.financial.service.api.transaction.persistence.TransactionEntity;
import be.loganfarci.financial.service.api.transaction.persistence.TransactionRepository;
import be.loganfarci.financial.service.api.transaction.model.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;
    private final TransactionCategoryService transactionCategoryService;

    public TransactionService(
            TransactionRepository transactionRepository,
            OwnerService ownerService,
            BankAccountService bankAccountService,
            TransactionCategoryService transactionCategoryService
    ) {
        this.transactionRepository = transactionRepository;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
        this.transactionCategoryService = transactionCategoryService;
    }

    private TransactionEntity findEntityById(Long id) {
        Optional<TransactionEntity> entity = transactionRepository.findById(id);
        return entity.orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public Transaction findById(Long id) {
        return fromEntity(findEntityById(id));
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public Transaction fromEntity(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getDate(),
                entity.getInternalBankAccount().getId(),
                entity.getExternalBankAccount().getId(),
                entity.getAmount(),
                entity.getDescription(),
                entity.getDescription()
        );
    }

    private boolean hasExistingInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getInternalBankAccount());
    }

    private BankAccountEntity findInternalBankAccount(TransactionDto transaction) {
        return bankAccountService.findEntity(transaction.getInternalBankAccount());
    }

    private boolean hasExistingExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.exists(transaction.getExternalBankAccount());
    }

    private BankAccountEntity findExternalBankAccount(TransactionDto transaction) {
        return bankAccountService.findEntity(transaction.getExternalBankAccount());
    }

    public TransactionEntity save(TransactionDto transaction) {
        TransactionEntity entity;
        BankAccountEntity internalBankAccount;
        BankAccountEntity externalBankAccount;
        TransactionCategoryEntity category = null;

        if (transaction == null) {
            throw new IllegalArgumentException("A transaction is required.");
        }

        if (transaction.getInternalBankAccount() == null) {
            throw new IllegalArgumentException("An internal bank account is required.");
        }

        if (transaction.getExternalBankAccount() == null) {
            throw new IllegalArgumentException("An external bank account is required.");
        }

        if (hasExistingInternalBankAccount(transaction)) {
            internalBankAccount = findInternalBankAccount(transaction);
        } else {
            throw new BankAccountEntityNotFoundException("Internal bank account not found.");
        }

        if (hasExistingExternalBankAccount(transaction)) {
            externalBankAccount = findExternalBankAccount(transaction);
        } else {
            throw new BankAccountEntityNotFoundException("External bank account not found.");
        }

        if (transactionCategoryService.exists(transaction.getCategory())) {
            category = transactionCategoryService.find(transaction.getCategory());
        }

        if (transactionRepository.existsById(transaction.getId())) {
            entity = findEntityById(transaction.getId());
        } else {
            entity = new TransactionEntity();
        }

        entity.setDate(transaction.getDate());
        entity.setInternalBankAccount(internalBankAccount);
        entity.setExternalBankAccount(externalBankAccount);
        entity.setAmount(transaction.getAmount());
        entity.setDescription(transaction.getDescription());
        entity.setCategory(category);

        return transactionRepository.save(entity);
    }

    public void save(WriteTransactionDto values) {
        save(fromWriteTransactionDto(values));
    }

    public void updateById(Long id, WriteTransactionDto values) {
        if (!id.equals(values.getId())) {
            throw new TransactionBadRequestException("Invalid id: " + values.getId());
        }
        if (transactionRepository.existsById(id)) {
            save(fromWriteTransactionDto(values));
        } else {
            throw new TransactionNotFoundException(id);
        }
    }

    public void deleteById(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        } else {
            throw new TransactionNotFoundException(id);
        }
    }

    private TransactionDto fromWriteTransactionDto(WriteTransactionDto values) {
        BankAccountDto internal = bankAccountService.findById(values.getInternalBankAccountId());
        BankAccountDto external = bankAccountService.findById(values.getExternalBankAccountId());
        TransactionCategoryDto category = transactionCategoryService.findDtoByName(values.getCategoryName());
        return new TransactionDto(
                values.getId(),
                values.getDate(),
                internal,
                external,
                values.getAmount(),
                values.getDescription(),
                category
        );
    }

}
