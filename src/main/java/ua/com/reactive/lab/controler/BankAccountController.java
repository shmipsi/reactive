package ua.com.reactive.lab.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.dto.AmountRequest;
import ua.com.reactive.lab.entity.BankAccount;
import ua.com.reactive.lab.entity.BankAccountStatus;
import ua.com.reactive.lab.entity.CreditCard;
import ua.com.reactive.lab.entity.Payment;
import ua.com.reactive.lab.service.BankAccountService;
import ua.com.reactive.lab.service.CreditCardService;
import ua.com.reactive.lab.service.PaymentService;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final CreditCardService creditCardService;
    private final PaymentService paymentService;

    @GetMapping
    public Flux<BankAccount> list(@RequestParam(required = false) BankAccountStatus status) {
        return status == null ? bankAccountService.findAll() : bankAccountService.findByStatus(status);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Mono<BankAccount> get(@PathVariable Long id) {
        return bankAccountService.findById(id);
    }

    @GetMapping("/{id}/cards")
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Flux<CreditCard> cards(@PathVariable Long id) {
        return creditCardService.findByAccountId(id);
    }

    @GetMapping("/{id}/payments")
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Flux<Payment> payments(@PathVariable Long id) {
        return paymentService.findByAccountId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@access.isSelf(authentication, #account.userId)")
    public Mono<BankAccount> create(@RequestBody BankAccount account) {
        return bankAccountService.create(account);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Mono<BankAccount> update(@PathVariable Long id, @RequestBody BankAccount account) {
        return bankAccountService.update(id, account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Mono<Void> delete(@PathVariable Long id) {
        return bankAccountService.delete(id);
    }

    @PatchMapping("/{id}/block")
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Mono<BankAccount> block(@PathVariable Long id) {
        return bankAccountService.block(id);
    }

    @PatchMapping("/{id}/unblock")
    public Mono<BankAccount> unblock(@PathVariable Long id) {
        return bankAccountService.unblock(id);
    }

    @PostMapping("/{id}/refill")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@access.ownsAccount(authentication, #id)")
    public Mono<Payment> refill(@PathVariable Long id, @RequestBody AmountRequest request) {
        return paymentService.refill(id, request);
    }
}
