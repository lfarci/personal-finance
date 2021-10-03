package be.loganfarci.financial.service.api.category.web;

import be.loganfarci.financial.service.api.category.service.TransactionCategoryService;
import be.loganfarci.financial.service.api.category.model.dto.TransactionCategory;
import be.loganfarci.financial.service.api.category.model.dto.TransactionCategoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TransactionCategoryController {

    private final TransactionCategoryService transactionCategoryService;

    public TransactionCategoryController(TransactionCategoryService transactionCategoryService) {
        this.transactionCategoryService = transactionCategoryService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/transactions/categories/{name}")
    public TransactionCategory findByName(@PathVariable String name) {
        return transactionCategoryService.findByName(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/transactions/categories")
    public List<TransactionCategory> findAll() {
        return transactionCategoryService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transactions/categories")
    public TransactionCategoryDto save(@RequestBody TransactionCategory values) {
        return transactionCategoryService.save(values);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/transactions/categories/{name}")
    public void updateByName(
            @PathVariable String name,
            @RequestBody TransactionCategory values
    ) {
        transactionCategoryService.update(name, values);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/transactions/categories/{name}")
    public void deleteByName(@PathVariable String name) {
        transactionCategoryService.deleteByName(name);
    }

}
