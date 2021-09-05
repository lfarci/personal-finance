package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityIsInvalidException;
import be.loganfarci.financial.service.api.owner.exception.OwnerNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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

    private OwnerEntity requireValidEntity(OwnerDto owner) {
        Objects.requireNonNull(owner, ErrorMessage.REQUIRED_ENTITY);
        Objects.requireNonNull(owner.getName(), ErrorMessage.REQUIRED_OWNER_NAME);
        return new OwnerEntity(requireValidOwnerName(owner.getName()));
    }

    public boolean existsByName(String name) {
        return ownerRepository.existsById(name);
    }

    public OwnerEntity findByName(String name) {
        Optional<OwnerEntity> entity = ownerRepository.findById(name);
        return entity.orElseThrow(() -> new OwnerNotFoundException(name));
    }

    public OwnerEntity save(OwnerDto owner) {
        OwnerEntity entity = requireValidEntity(owner);
        if (existsByName(owner.getName())) {
            throw new OwnerEntityAlreadyExistsException(owner.getName());
        } else {
            return ownerRepository.save(entity);
        }
    }

}
