package be.loganfarci.financial.service.api.account.web;

import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountRequestBodyDto;
import be.loganfarci.financial.service.api.transaction.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    public BankAccountController(BankAccountService bankAccountService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccountDto> find(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.findById(id));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDto>> findAll(@RequestParam(name = "internal") Boolean isInternal) {
        return ResponseEntity.ok(bankAccountService.findAll(isInternal));
    }

    @PostMapping("/accounts")
    public ResponseEntity<BankAccountDto> save(@Valid @RequestBody BankAccountRequestBodyDto requestBodyDto) {
        return new ResponseEntity<>(bankAccountService.save(requestBodyDto), HttpStatus.CREATED);
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<BankAccountDto> update(
            @PathVariable Long id,
            @Valid @RequestBody BankAccountRequestBodyDto requestBodyDto
    ) {
        return new ResponseEntity<>(bankAccountService.update(id, requestBodyDto), HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        transactionService.deleteAllForBankAccountId(id);
        bankAccountService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
