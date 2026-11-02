package ua.com.reactive.lab.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.BankAccount;
import ua.com.reactive.lab.entity.User;
import ua.com.reactive.lab.service.BankAccountService;
import ua.com.reactive.lab.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BankAccountService bankAccountService;

    @GetMapping
    public Flux<User> list() {
        return userService.findAll();
    }

    @GetMapping("/me")
    public Mono<User> me(@AuthenticationPrincipal User principal) {
        return userService.findById(principal.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@access.isSelf(authentication, #id)")
    public Mono<User> get(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/{id}/accounts")
    @PreAuthorize("@access.isSelf(authentication, #id)")
    public Flux<BankAccount> accounts(@PathVariable Long id) {
        return bankAccountService.findByUserId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<User> create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@access.isSelf(authentication, #id)")
    public Mono<User> update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
