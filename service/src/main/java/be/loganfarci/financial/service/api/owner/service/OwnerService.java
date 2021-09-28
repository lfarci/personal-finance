package be.loganfarci.financial.service.api.owner.service;

import be.loganfarci.financial.service.api.owner.model.dto.OwnerDto;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerAlreadyExistsException;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerIsInvalidException;
import be.loganfarci.financial.service.api.owner.model.exception.OwnerNotFoundException;
import be.loganfarci.financial.service.api.owner.persistence.OwnerEntity;
import be.loganfarci.financial.service.api.owner.persistence.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.loganfarci.financial.service.api.owner.persistence.OwnerEntity.NAME_COLUMN_LENGTH;

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

    public boolean existsByName(String name) {
        return ownerRepository.existsById(name);
    }

    public OwnerEntity findEntityByName(String name) {
        Optional<OwnerEntity> entity = ownerRepository.findById(name);
        return entity.orElseThrow(() -> new OwnerNotFoundException(name));
    }

    public OwnerDto findByName(String name) {
        OwnerEntity entity = findEntityByName(name);
        return new OwnerDto(entity.getName());
    }

    public List<OwnerDto> findAll() {
        return ownerRepository.findAll()
                .stream()
                .map(e -> new OwnerDto(e.getName()))
                .collect(Collectors.toList());
    }

    public OwnerEntity save(OwnerDto owner) {
        OwnerEntity entity = requireValidEntity(owner);
        if (existsByName(owner.getName())) {
            throw new OwnerAlreadyExistsException(owner.getName());
        } else {
            return ownerRepository.save(entity);
        }
    }

    public void deleteByName(String name) {
        requireValidOwnerName(name);
        if (existsByName(name)) {
            ownerRepository.deleteById(name);
        } else {
            throw new OwnerNotFoundException(name);
        }
    }

    private boolean isValidOwnerName(String ownerName) {
        return ownerName != null && !ownerName.isBlank() && ownerName.length() <= NAME_COLUMN_LENGTH;
    }

    private String requireValidOwnerName(String ownerName) {
        if (!isValidOwnerName(ownerName)) {
            throw new OwnerIsInvalidException("\"" + ownerName + "\" is not a valid name");
        }
        return ownerName;
    }

    private OwnerEntity requireValidEntity(OwnerDto owner) {
        Objects.requireNonNull(owner, ErrorMessage.REQUIRED_ENTITY);
        Objects.requireNonNull(owner.getName(), ErrorMessage.REQUIRED_OWNER_NAME);
        return new OwnerEntity(requireValidOwnerName(owner.getName()));
    }

    private OwnerDto requireValid(OwnerDto owner) {
        Objects.requireNonNull(owner, ErrorMessage.REQUIRED_ENTITY);
        Objects.requireNonNull(owner.getName(), ErrorMessage.REQUIRED_OWNER_NAME);
        requireValidOwnerName(owner.getName());
        return owner;
    }

}
