package be.loganfarci.financial.service.api.accounts.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);
    List<BankAccountEntity> findByUserId(Long userId);
}
