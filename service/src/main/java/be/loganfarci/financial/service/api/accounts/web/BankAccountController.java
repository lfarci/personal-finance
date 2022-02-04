package be.loganfarci.financial.service.api.accounts.web;

import be.loganfarci.financial.service.api.accounts.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.accounts.service.BankAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Page<BankAccountDto> findByUserId(
            @PathVariable Long userId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        return bankAccountService.findByUserId(userId, PageRequest.of(page, size));
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
