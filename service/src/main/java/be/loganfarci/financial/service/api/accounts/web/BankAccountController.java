package be.loganfarci.financial.service.api.accounts.web;

import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/accounts/{bankAccountId}")
    public BankAccountDto findByUserIdAndBankAccountId(@PathVariable Long userId, @PathVariable Long bankAccountId) {
        return bankAccountService.findByIdAndUserId(userId, bankAccountId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}/accounts")
    public List<BankAccountDto> findByUserId(@PathVariable Long userId) {
        return bankAccountService.findByUserId(userId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/accounts/{bankAccountId}")
    public void deleteByIdAndUserId(@PathVariable Long userId, @PathVariable Long bankAccountId) {
        bankAccountService.deleteByIdAndUserId(userId, bankAccountId);
    }

//    @GetMapping("/accounts")
//    public ResponseEntity<List<BankAccountDto>> findAll(@RequestParam(name = "internal") Boolean isInternal) {
//        return ResponseEntity.ok(oldBankAccountService.findAll(isInternal));
//    }
//
//    @PostMapping("/accounts")
//    public ResponseEntity<BankAccountDto> save(@Valid @RequestBody BankAccountRequestBodyDto requestBodyDto) {
//        return new ResponseEntity<>(oldBankAccountService.save(requestBodyDto), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/accounts/{id}")
//    public ResponseEntity<BankAccountDto> update(
//            @PathVariable Long id,
//            @Valid @RequestBody BankAccountRequestBodyDto requestBodyDto
//    ) {
//        return new ResponseEntity<>(oldBankAccountService.update(id, requestBodyDto), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/accounts/{id}")
//    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
//        transactionService.deleteAllForBankAccountId(id);
//        oldBankAccountService.deleteById(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
