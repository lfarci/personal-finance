package be.loganfarci.financial.service.api.accounts.service;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.persistence.BankAccountEntity;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {

    public BankAccountDto toRest(BankAccountEntity entity) {
        return new BankAccountDto(
                entity.getId(),
                entity.getName(),
                entity.getUser().getId(),
                entity.getIban(),
                entity.getBalance());
    }

    public BankAccountEntity fromRest(BankAccountDto user) {
        return null;
    }


}