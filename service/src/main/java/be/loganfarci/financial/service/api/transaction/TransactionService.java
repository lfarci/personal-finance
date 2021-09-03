package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.model.BankAccount;
import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.service.api.account.BankAccountEntity;
import be.loganfarci.financial.service.api.account.BankAccountRepository;
import be.loganfarci.financial.service.api.account.BankAccountService;
import be.loganfarci.financial.service.api.owner.OwnerEntity;
import be.loganfarci.financial.service.api.owner.OwnerService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Objects;

@Service
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;

    public TransactionService(
            TransactionMapper mapper,
            TransactionRepository transactionRepository,
            OwnerService ownerService,
            BankAccountService bankAccountService
    ) {
        this.transactionMapper = mapper;
        this.transactionRepository = transactionRepository;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
    }

    public void save(Transaction transaction) {
        Objects.requireNonNull(transaction, "A transaction is required");
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setDate(new Date(transaction.getDate().getTime()));
        transactionEntity.setSender(bankAccountService.save(transaction.getInternalBankAccount()));
        transactionEntity.setRecipient(bankAccountService.save(transaction.getExternalBankAccount()));
        transactionEntity.setAmount(transaction.getAmount());
        transactionEntity.setDescription(transaction.getDescription());
        transactionRepository.save(transactionEntity);
    }

}
