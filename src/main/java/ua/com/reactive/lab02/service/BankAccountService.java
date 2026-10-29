package ua.com.reactive.lab02.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.com.reactive.lab02.entity.BankAccount;
import ua.com.reactive.lab02.entity.BankAccountStatus;
import ua.com.reactive.lab02.repository.BankAccountRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;
    public Flux<BankAccount> findAll() {
        return bankAccountRepository.findAll();
    }
    public Flux<BankAccount> findByStatus(BankAccountStatus status) {
        return bankAccountRepository.findAllByStatus(status);
    }
    public Flux<BankAccount> findByUserId(Long userId) {
        return userService.findById(userId)
                .flatMapMany(user -> bankAccountRepository.findAllByUserId(userId));
    }
    public Mono<BankAccount> findById(Long id) {
        return bankAccountRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Account " + id + " not found")));
    }
    public Mono<BankAccount> create(BankAccount account) {
        account.setId(null);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(BankAccountStatus.ACTIVE);
        return userService.findById(account.getUserId())
                .flatMap(user -> bankAccountRepository.save(account));
    }
    // balance and status are changed only through refill/payment and block/unblock
    public Mono<BankAccount> update(Long id, BankAccount account) {
        return findById(id).flatMap(existing -> {
            existing.setNumber(account.getNumber());
            return bankAccountRepository.save(existing);
        });
    }
    public Mono<Void> delete(Long id) {
        return findById(id).flatMap(bankAccountRepository::delete);
    }
    public Mono<BankAccount> block(Long id) {
        return findById(id).flatMap(account -> {
            if (account.getStatus() == BankAccountStatus.BLOCKED) {
                return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Account " + id + " is already blocked"));
            }
            account.setStatus(BankAccountStatus.BLOCKED);
            return bankAccountRepository.save(account);
        });
    }
    public Mono<BankAccount> unblock(Long id, Long adminId) {
        return userService.requireAdmin(adminId)
                .then(findById(id))
                .flatMap(account -> {
                    if (account.getStatus() == BankAccountStatus.ACTIVE) {
                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Account " + id + " is not blocked"));
                    }
                    account.setStatus(BankAccountStatus.ACTIVE);
                    return bankAccountRepository.save(account);
                });
    }
    public Mono<BankAccount> save(BankAccount account) {
        return bankAccountRepository.save(account);
    }
}
