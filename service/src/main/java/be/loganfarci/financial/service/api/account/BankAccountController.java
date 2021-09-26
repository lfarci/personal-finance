package be.loganfarci.financial.service.api.account;

import be.loganfarci.financial.service.api.account.dto.BankAccountDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/accounts/{id}")
    public ResponseEntity<BankAccountDto> find(@PathVariable Long id) {
        return ResponseEntity.ok(bankAccountService.findById(id));
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<BankAccountDto>> findAll() {
        return ResponseEntity.ok(bankAccountService.findAll());
    }

    @PostMapping("/accounts")
    public ResponseEntity<BankAccountDto> save(@RequestBody BankAccountDto bankAccountDto) {
        return new ResponseEntity<>(bankAccountService.put(bankAccountDto), HttpStatus.CREATED);
    }

    @PutMapping("/accounts")
    public ResponseEntity<BankAccountDto> update(@RequestBody BankAccountDto bankAccountDto) {
        return new ResponseEntity<>(bankAccountService.put(bankAccountDto), HttpStatus.OK);
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        bankAccountService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
