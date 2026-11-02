package ua.com.reactive.lab.controler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab.entity.CreditCard;
import ua.com.reactive.lab.service.CreditCardService;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardService creditCardService;

    @GetMapping
    public Flux<CreditCard> list() {
        return creditCardService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@access.ownsCard(authentication, #id)")
    public Mono<CreditCard> get(@PathVariable Long id) {
        return creditCardService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@access.ownsAccount(authentication, #card.accountId)")
    public Mono<CreditCard> create(@RequestBody CreditCard card) {
        return creditCardService.create(card);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@access.canUpdateCard(authentication, #id, #card.accountId)")
    public Mono<CreditCard> update(@PathVariable Long id, @RequestBody CreditCard card) {
        return creditCardService.update(id, card);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@access.ownsCard(authentication, #id)")
    public Mono<Void> delete(@PathVariable Long id) {
        return creditCardService.delete(id);
    }
}
