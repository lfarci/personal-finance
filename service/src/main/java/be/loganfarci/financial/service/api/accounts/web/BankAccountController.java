package be.loganfarci.financial.service.api.accounts.web;

import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
@Validated
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/{userId}/accounts")
    public BankAccountDto saveForUserId(@PathVariable Long userId, @Valid @RequestBody BankAccountDto bankAccount) {
        return bankAccountService.saveForUserId(userId, bankAccount);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{userId}/accounts/{bankAccountId}")
    public void updateForUserId(
            @PathVariable Long userId,
            @PathVariable Long bankAccountId,
            @Valid @RequestBody BankAccountDto bankAccount
    ) {
        bankAccountService.updateByIdAndUserUd(userId, bankAccountId, bankAccount);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/accounts/{bankAccountId}")
    public void deleteByIdAndUserId(@PathVariable Long userId, @PathVariable Long bankAccountId) {
        bankAccountService.deleteByIdAndUserId(userId, bankAccountId);
    }

}
