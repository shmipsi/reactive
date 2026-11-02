package ua.com.reactive.lab.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ua.com.reactive.lab.entity.BankAccount;
import ua.com.reactive.lab.entity.BankAccountStatus;

public interface BankAccountRepository extends ReactiveCrudRepository<BankAccount, Long> {
    Flux<BankAccount> findAllByUserId(Long userId);
    Flux<BankAccount> findAllByStatus(BankAccountStatus status);
}
