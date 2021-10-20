package be.loganfarci.financial.service.api.transaction.web;

import be.loganfarci.financial.service.api.transaction.model.dto.Transaction;
import be.loganfarci.financial.service.api.transaction.model.dto.WriteTransactionDto;
import be.loganfarci.financial.service.api.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/transactions/{transactionId}")
    public Transaction findById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/transactions")
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transactions")
    public void save(@RequestBody WriteTransactionDto values) {
        transactionService.save(values);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/transactions/{transactionId}")
    public void updateById(
            @PathVariable Long transactionId,
            @RequestBody WriteTransactionDto values
    ) {
        transactionService.updateById(transactionId, values);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/transactions/{transactionId}")
    public void deleteById(@PathVariable Long transactionId) {
        transactionService.deleteById(transactionId);
    }

}
