package be.loganfarci.financial.service.api.users.web;

import be.loganfarci.financial.service.api.users.model.UserDto;
import be.loganfarci.financial.service.api.users.service.UserService;
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
        System.out.println("trying to get user by id");
        return service.findById(userId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    public List<UserDto> findAll() {
        return service.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users")
    public UserDto save(@Valid @RequestBody UserDto user) {
        return service.save(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}")
    public void deleteById(@PathVariable Long userId) {
        service.deleteById(userId);
    }

}
