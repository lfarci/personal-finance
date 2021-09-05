package be.loganfarci.financial.service.api.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BankAccountRepository extends CrudRepository<BankAccountEntity, Long> {
    boolean existsByNameAndOwnerName(String name, String ownerName);
    Optional<BankAccountEntity> findByNameAndOwnerName(String name, String ownerName);
}
