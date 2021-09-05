package be.loganfarci.financial.service.api.transaction;

import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.csv.model.Transaction;
import be.loganfarci.financial.service.api.account.BankAccountEntity;
import be.loganfarci.financial.service.api.account.BankAccountMapper;
import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Objects;

import static be.loganfarci.financial.service.api.owner.OwnerEntity.NAME_COLUMN_LENGTH;

@Component
public class TransactionMapper {

    private final BankAccountMapper bankAccountMapper;

    public TransactionMapper(BankAccountMapper mapper) {
        this.bankAccountMapper = mapper;
    }

    private OwnerDto toDto(Owner owner) {
        String name;
        if (owner.getName().length() > NAME_COLUMN_LENGTH) {
            name = owner.getName().substring(0, NAME_COLUMN_LENGTH);
        }
        return new OwnerDto(owner.getName());
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
