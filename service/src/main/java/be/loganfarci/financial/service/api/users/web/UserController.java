package be.loganfarci.financial.service.api.users.web;

import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{userId}")
    public UserDto findById(@PathVariable Long userId) {
        return service.findById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public Page<UserDto> findAll(
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        Pageable request = PageRequest.of(page, size, Sort.by("creationDate").descending());
        return service.findAll(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDto save(@Valid @RequestBody UserDto user) {
        return service.save(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/users/{userId}")
    public void save(@PathVariable Long userId, @Valid @RequestBody UserDto user) {
        service.updateById(userId, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    public void deleteById(@PathVariable Long userId) {
        service.deleteById(userId);
    }

}
