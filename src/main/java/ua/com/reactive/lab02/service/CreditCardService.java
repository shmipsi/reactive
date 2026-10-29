package ua.com.reactive.lab02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.entity.CreditCard;
import ua.com.reactive.lab02.repository.CreditCardRepository;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final BankAccountService bankAccountService;

    public Flux<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    public Flux<CreditCard> findByAccountId(Long accountId) {
        return bankAccountService.findById(accountId)
                .flatMapMany(account -> creditCardRepository.findAllByAccountId(accountId));
    }

    public Mono<CreditCard> findById(Long id) {
        return creditCardRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Card " + id + " not found")));
    }

    public Mono<CreditCard> create(CreditCard card) {
        card.setId(null);
        return bankAccountService.findById(card.getAccountId())
                .flatMap(account -> creditCardRepository.save(card));
    }

    public Mono<CreditCard> update(Long id, CreditCard card) {
        return findById(id)
                .zipWith(bankAccountService.findById(card.getAccountId()))
                .flatMap(tuple -> {
                    CreditCard existing = tuple.getT1();
                    existing.setNumber(card.getNumber());
                    existing.setExpiryDate(card.getExpiryDate());
                    existing.setAccountId(card.getAccountId());
                    return creditCardRepository.save(existing);
                });
    }

    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(creditCardRepository::delete);
    }
}
