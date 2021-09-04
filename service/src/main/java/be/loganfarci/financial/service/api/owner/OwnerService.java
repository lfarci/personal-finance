package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public OwnerEntity save(Owner owner) {
        OwnerEntity entity = new OwnerEntity();
        entity.setName(owner.getName());
        return ownerRepository.save(entity);
    }
}
