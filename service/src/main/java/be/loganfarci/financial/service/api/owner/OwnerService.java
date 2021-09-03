package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerMapper ownerMapper, OwnerRepository ownerRepository) {
        this.ownerMapper = ownerMapper;
        this.ownerRepository = ownerRepository;
    }

    public OwnerEntity save(Owner owner) {
        OwnerEntity entity = null;
        if (owner != null && !owner.getName().isBlank()) {
            entity = ownerRepository.save(ownerMapper.toEntity(owner));
        }
        return entity;
    }
}
