package be.loganfarci.financial.service.api.accounts.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccountEntity, Long> {
    boolean existsByIdAndUserId(Long id, Long userId);
    boolean existsByIban(String iban);
    List<BankAccountEntity> findByUserId(Long userId);
    Page<BankAccountEntity> findByUserIdAndInternalIsTrue(Long userId, Pageable pageable);
}
