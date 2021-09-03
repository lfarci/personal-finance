package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.service.api.account.BankAccountEntity;
import be.loganfarci.financial.service.api.account.BankAccountMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Objects;

@Component
public class TransactionMapper {

    private final BankAccountMapper bankAccountMapper;

    public TransactionMapper(BankAccountMapper mapper) {
        this.bankAccountMapper = mapper;
    }

    public TransactionEntity toEntity(Transaction transaction) {
        Objects.requireNonNull(transaction, "A transaction is required.");
        Date date = new Date(transaction.getDate().getTime());
        BankAccountEntity internalBankAccount = bankAccountMapper.toEntity(transaction.getInternalBankAccount());
        BankAccountEntity externalBankAccount = bankAccountMapper.toEntity(transaction.getExternalBankAccount());
        Double amount = transaction.getAmount();
        String description = transaction.getDescription();
        return new TransactionEntity(date, internalBankAccount, externalBankAccount, amount, description);
    }

}
