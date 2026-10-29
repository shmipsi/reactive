package ua.com.reactive.lab02.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.dto.AmountRequest;
import ua.com.reactive.lab02.entity.BankAccount;
import ua.com.reactive.lab02.entity.BankAccountStatus;
import ua.com.reactive.lab02.entity.CreditCard;
import ua.com.reactive.lab02.entity.Payment;
import ua.com.reactive.lab02.service.BankAccountService;
import ua.com.reactive.lab02.service.CreditCardService;
import ua.com.reactive.lab02.service.PaymentService;

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
    public Mono<BankAccount> get(@PathVariable Long id) {
        return bankAccountService.findById(id);
    }

    @GetMapping("/{id}/cards")
    public Flux<CreditCard> cards(@PathVariable Long id) {
        return creditCardService.findByAccountId(id);
    }

    @GetMapping("/{id}/payments")
    public Flux<Payment> payments(@PathVariable Long id) {
        return paymentService.findByAccountId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<BankAccount> create(@RequestBody BankAccount account) {
        return bankAccountService.create(account);
    }

    @PutMapping("/{id}")
    public Mono<BankAccount> update(@PathVariable Long id, @RequestBody BankAccount account) {
        return bankAccountService.update(id, account);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return bankAccountService.delete(id);
    }

    @PatchMapping("/{id}/block")
    public Mono<BankAccount> block(@PathVariable Long id) {
        return bankAccountService.block(id);
    }

    @PatchMapping("/{id}/unblock")
    public Mono<BankAccount> unblock(@PathVariable Long id, @RequestHeader("X-Admin-Id") Long adminId) {
        return bankAccountService.unblock(id, adminId);
    }

    @PostMapping("/{id}/refill")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Payment> refill(@PathVariable Long id, @RequestBody AmountRequest request) {
        return paymentService.refill(id, request);
    }
}
