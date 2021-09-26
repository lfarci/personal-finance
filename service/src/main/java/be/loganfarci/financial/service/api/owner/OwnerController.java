package be.loganfarci.financial.service.api.owner;

import be.loganfarci.financial.service.api.owner.dto.OwnerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/owners/{name}")
    public ResponseEntity<OwnerDto> findOwner(@PathVariable String name) {
        return ResponseEntity.ok(ownerService.findByName(name));
    }

    @GetMapping("/owners")
    public ResponseEntity<List<OwnerDto>> findAll() {
        return ResponseEntity.ok(ownerService.findAll());
    }

    @PostMapping("/owners")
    public ResponseEntity<OwnerDto> saveOwner(@RequestBody OwnerDto owner) {
        ownerService.save(owner);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/owners/{name}")
    public void deleteOwner(@PathVariable String name) {
        ownerService.deleteByName(name);
    }
}
