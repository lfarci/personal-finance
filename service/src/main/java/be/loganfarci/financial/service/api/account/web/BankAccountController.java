package be.loganfarci.financial.service.api.account.web;

import be.loganfarci.financial.service.api.account.service.BankAccountService;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountDto;
import be.loganfarci.financial.service.api.account.model.dto.BankAccountRequestBodyDto;
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

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccountDto> find(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.findById(id));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDto>> findAll() {
        return ResponseEntity.ok(bankAccountService.findAll());
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
        bankAccountService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
