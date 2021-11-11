package be.loganfarci.financial.service.api.accounts.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsByNameAndOwnerName(String name, String ownerName);
    boolean existsByIban(String iban);
    Optional<BankAccountEntity> findByNameAndOwnerName(String name, String ownerName);
    Optional<BankAccountEntity> findByIban(String iban);
    List<BankAccountEntity> findByUserId(Long userId);
}
