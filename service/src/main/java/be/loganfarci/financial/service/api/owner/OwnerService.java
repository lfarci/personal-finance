package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.csv.model.Owner;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.exception.OwnerEntityConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OwnerService {

    static class ErrorMessage {
        final static String REQUIRED_ENTITY = "An entity is required but was null.";
        final static String REQUIRED_OWNER_NAME = "An owner is required to have a name.";
        final static String INVALID_OWNER_NAME = "\"%s\" is an invalid owner name.";
    }

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    private OwnerEntity save(OwnerEntity entity) {
        try {
            return ownerRepository.saveAndFlush(entity);
        } catch (DataAccessException e) {
            String message = String.format(ErrorMessage.INVALID_OWNER_NAME, entity.getName());
            throw new OwnerEntityConstraintViolationException(message, e);
        }
    }

    public OwnerEntity save(Owner owner) {
        Objects.requireNonNull(owner, ErrorMessage.REQUIRED_ENTITY);
        Objects.requireNonNull(owner.getName(), ErrorMessage.REQUIRED_OWNER_NAME);
        if (ownerRepository.existsById(owner.getName())) {
            throw new OwnerEntityAlreadyExistsException(owner.getName());
        } else {
            return save(new OwnerEntity(owner.getName()));
        }
    }

}
