package be.loganfarci.financial.service.api.category.service;

import be.loganfarci.financial.service.api.category.model.dto.TransactionCategory;
import be.loganfarci.financial.service.api.category.model.dto.TransactionCategoryDto;
import be.loganfarci.financial.service.api.category.model.exception.InvalidTransactionCategoryException;
import be.loganfarci.financial.service.api.category.model.exception.TransactionCategoryEntityAlreadyExistsException;
import be.loganfarci.financial.service.api.category.model.exception.TransactionCategoryEntityNotFoundException;
import be.loganfarci.financial.service.api.category.persistence.TransactionCategoryEntity;
import be.loganfarci.financial.service.api.category.persistence.TransactionCategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionCategoryService {

    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionCategoryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    public boolean exists(TransactionCategoryDto category) {
        return category != null && transactionCategoryRepository.existsById(category.getName());
    }

    public TransactionCategoryDto findDtoByName(String name) {
        return fromEntity(find(name));
    }

    public TransactionCategory findByName(String name) {
        return toTransactionCategory(find(name));
    }

    public List<TransactionCategory> findAll() {
        return transactionCategoryRepository.findAll()
                .stream()
                .map(this::toTransactionCategory)
                .collect(Collectors.toList());
    }

    public TransactionCategoryEntity find(String name) {
        return transactionCategoryRepository
                .findById(name)
                .orElseThrow(() -> new TransactionCategoryEntityNotFoundException(name));
    }

    public TransactionCategoryEntity find(TransactionCategoryDto category) {
        return find(category.getName());
    }

    public TransactionCategoryDto save(TransactionCategory values) {
        return fromEntity(save(fromRequestBody(values)));
    }

    public TransactionCategoryEntity save(TransactionCategoryDto category) {
        TransactionCategoryEntity entity;
        if (category == null) {
            throw new IllegalArgumentException("A category is required.");
        }
        if (exists(category)) {
            throw new TransactionCategoryEntityAlreadyExistsException(category.getName());
        } else {
            entity = new TransactionCategoryEntity();
        }
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        if (category.getParent() != null) {
            entity.setParent(find(category.getParent()));
        }
        return transactionCategoryRepository.save(entity);
    }

    public void update(String name, TransactionCategory category) {
        if (!name.equals(category.getName())) {
            throw new InvalidTransactionCategoryException(category.getName());
        }
        if (transactionCategoryRepository.existsById(name)) {
            update(fromRequestBody(category));
        } else {
            throw new TransactionCategoryEntityNotFoundException(name);
        }
    }

    public TransactionCategoryEntity update(TransactionCategoryDto category) {
        TransactionCategoryEntity entity;
        if (category == null) {
            throw new IllegalArgumentException("A category is required.");
        }
        if (exists(category)) {
            entity = find(category);
        } else {
            throw new TransactionCategoryEntityNotFoundException(category.getName());
        }
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        if (category.getParent() != null) {
            entity.setParent(find(category.getParent()));
        }
        return transactionCategoryRepository.save(entity);
    }

    public void deleteByName(String name) {
        if (transactionCategoryRepository.existsById(name)) {
            transactionCategoryRepository.deleteById(name);
        } else {
            throw new TransactionCategoryEntityNotFoundException(name);
        }
    }

    private TransactionCategoryDto fromEntity(TransactionCategoryEntity entity) {
        TransactionCategoryDto parent = null;

        if (entity.getParent() != null) {
            parent = new TransactionCategoryDto(entity.getParent().getName(), entity.getParent().getDescription(), null);
        }

        return new TransactionCategoryDto(entity.getName(), entity.getDescription(), parent);
    }

    private TransactionCategory toTransactionCategory(TransactionCategoryEntity entity) {
        String parentName = null;
        if (entity.getParent() != null) {
            parentName = entity.getParent().getName();
        }
        return new TransactionCategory(entity.getName(), entity.getDescription(), parentName);
    }


    private TransactionCategoryDto fromRequestBody(TransactionCategory saveDto) {
        TransactionCategoryDto parent = null;

        if (saveDto.getParentName() != null && transactionCategoryRepository.existsById(saveDto.getParentName())) {
            parent = findDtoByName(saveDto.getParentName());
        }

        if (saveDto.getParentName() != null && !transactionCategoryRepository.existsById(saveDto.getParentName())) {
            throw new TransactionCategoryEntityNotFoundException(saveDto.getParentName());
        }

        return new TransactionCategoryDto(saveDto.getName(), saveDto.getDescription(), parent);
    }

}
