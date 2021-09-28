package be.loganfarci.financial.service.api.owner.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<OwnerEntity, String> {
}