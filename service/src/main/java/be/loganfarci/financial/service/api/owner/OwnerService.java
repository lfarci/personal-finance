package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityIsInvalidException;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static be.loganfarci.financial.service.api.owner.OwnerEntity.NAME_COLUMN_LENGTH;

@Service
public class OwnerService {

    static class ErrorMessage {
        final static String REQUIRED_ENTITY = "An entity is required but was null.";
        final static String REQUIRED_OWNER_NAME = "An owner is required to have a name.";
    }

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    private boolean isValidOwnerName(String ownerName) {
        return ownerName != null && !ownerName.isBlank() && ownerName.length() <= NAME_COLUMN_LENGTH;
    }

    private String requireValidOwnerName(String ownerName) {
        if (!isValidOwnerName(ownerName)) {
            throw new OwnerEntityIsInvalidException(ownerName + " is not a valid name.");
        }
        return ownerName;
    }

    public OwnerEntity requireValidEntity(Owner owner) {
        Objects.requireNonNull(owner, ErrorMessage.REQUIRED_ENTITY);
        Objects.requireNonNull(owner.getName(), ErrorMessage.REQUIRED_OWNER_NAME);
        return new OwnerEntity(requireValidOwnerName(owner.getName()));
    }

    public OwnerEntity save(Owner owner) {
        OwnerEntity entity = requireValidEntity(owner);
        if (ownerRepository.existsById(owner.getName())) {
            throw new OwnerEntityAlreadyExistsException(owner.getName());
        } else {
            return ownerRepository.save(entity);
        }
    }

}
