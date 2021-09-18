package be.loganfarci.financial.service.api.category;

import be.loganfarci.financial.service.api.category.dto.TransactionCategoryDto;
import be.loganfarci.financial.service.api.category.exception.TransactionCategoryEntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TransactionCategoryService {

    private final TransactionCategoryRepository transactionCategoryRepository;

    public TransactionCategoryService(TransactionCategoryRepository transactionCategoryRepository) {
        this.transactionCategoryRepository = transactionCategoryRepository;
    }

    public boolean exists(TransactionCategoryDto category) {
        return transactionCategoryRepository.existsById(category.getName());
    }

    public TransactionCategoryEntity find(TransactionCategoryDto category) {
        return transactionCategoryRepository
                .findById(category.getName())
                .orElseThrow(() -> new TransactionCategoryEntityNotFoundException(category.getName()));
    }

    public TransactionCategoryEntity save(TransactionCategoryDto category) {
        TransactionCategoryEntity entity;

        if (category == null) {
            throw new IllegalArgumentException("A category is required.");
        }

        if (exists(category)) {
            entity = find(category);
        } else {
            entity = new TransactionCategoryEntity();
        }

        entity.setName(category.getName());
        entity.setDescription(category.getDescription());
        entity.setParent(find(category.getParent()));

        return transactionCategoryRepository.save(entity);
    }

}
