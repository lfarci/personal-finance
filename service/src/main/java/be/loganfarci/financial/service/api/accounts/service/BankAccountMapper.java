package be.loganfarci.financial.service.api.accounts.service;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity;
import be.loganfarci.financial.service.api.users.persistence.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {

    public BankAccountDto toRest(BankAccountEntity entity) {
        return new BankAccountDto(
                entity.getId(),
                entity.getName(),
                entity.getUser().getId(),
                entity.getIban(),
                entity.getBalance(),
                entity.isInternal(),
                entity.getOwnerName());
    }

    public BankAccountEntity fromRest(BankAccountDto bankAccount, UserEntity user) {
        return new BankAccountEntity(
                bankAccount.getId(),
                bankAccount.getName(),
                user,
                bankAccount.getOwnerName(),
                bankAccount.getIban(),
                bankAccount.getBalance(),
                bankAccount.isInternal()
        );
    }


}
